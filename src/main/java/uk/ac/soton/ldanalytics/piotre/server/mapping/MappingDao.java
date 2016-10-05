package uk.ac.soton.ldanalytics.piotre.server.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import uk.ac.soton.ldanalytics.piotre.server.util.Prefixes;

public class MappingDao {
	final String baseUri = "http://iot.soton.ac.uk/";
	final String CLASS_URI = "rdf:type";
	
	private Sql2o sql2o;
	
	public MappingDao(Sql2o sql2o) {
		this.sql2o = sql2o;
	}
	
	public Iterable<Mapping> getAllMappings() {
		try (Connection conn = sql2o.open()) {
            List<Mapping> mappings = conn.createQuery("select t1.id, t1.name, t1.author, t1.uri, t2.id2 as dataRelId, t3.name as dataRelName "
            		+ "from mappings t1, rel_mappings_data t2, data t3 "
            		+ "where t1.id=t2.id1 AND t2.id2=t3.id")
                    .executeAndFetch(Mapping.class);
            return mappings;
        }
    }
	
	public Mapping getMapping(String id) {
		try (Connection conn = sql2o.open()) {
            List<Mapping> mappings = conn.createQuery("select t1.id, t1.name, t1.author, t1.uri, t1.content, t1.format, t2.id2 as dataRelId, t3.name as dataRelName "
            		+ "from mappings t1, rel_mappings_data t2, data t3 "
            		+ "where t1.id='"+id+"' AND t1.id=t2.id1 AND t2.id2=t3.id")
                    .executeAndFetch(Mapping.class);
            return mappings.get(0);
        }
	}
	
	public String convertMappingContent(String content) {
		Map<String,Integer> bNodeReference = new HashMap<String,Integer>();
		Map<String,String> uriReference = new HashMap<String,String>();
		Map<String,JSONObject> classSimplification = new HashMap<String,JSONObject>();
		uriReference.putAll(Prefixes.Common.getMap());
		
		JSONObject predicates = new JSONObject();
		JSONObject classes = new JSONObject();
		JSONObject mappingJson = new JSONObject();
		JSONArray triples = new JSONArray();
		Model model = ModelFactory.createDefaultModel();
		RDFDataMgr.read(model, IOUtils.toInputStream(content), Lang.NTRIPLES);
		StmtIterator stmts = model.listStatements();
		while(stmts.hasNext()) {
			JSONObject triple = new JSONObject();
			Statement st = stmts.next();
			JSONObject s = convertSubject(st.getSubject(),bNodeReference,uriReference);
			JSONObject p = convertPredicate(st.getPredicate(),uriReference);
			JSONObject o = convertObject(st.getObject(),bNodeReference,uriReference);
			triple.put("s", s);
			triple.put("p", p);
			predicates.put(p.getString("val"),p.getString("raw"));
			triple.put("o", o);
			if(p.getString("val").equals(CLASS_URI)) {
				classes.put(o.getString("val"),o.getString("raw"));
				addClassSimplification(classSimplification,s,o);
			} else {
				triples.put(triple);
			}
		}
		JSONArray newTriples = new JSONArray();
		for (Object triple:triples) {
			JSONObject obj = (JSONObject) triple;
		    JSONObject sReplace = classSimplification.get(obj.getJSONObject("s").getString("val"));
		    JSONObject oReplace = classSimplification.get(obj.getJSONObject("o").getString("val"));
		    if(sReplace!=null) {
		    	obj.put("s", sReplace);
//		    	System.out.println(sReplace.toString());
		    }
		    if(oReplace!=null) {
		    	obj.put("o", oReplace);
		    }
		    newTriples.put(obj);
		}
		mappingJson.put("content", newTriples);
		mappingJson.put("predicates", predicates);
		mappingJson.put("classes", classes);
		return mappingJson.toString();
	}

	private void addClassSimplification(Map<String, JSONObject> classSimplification, JSONObject s, JSONObject o) {
		s.put("sem_class", o.getString("val"));
		s.put("sem_class_raw", o.getString("raw"));
		classSimplification.put(s.getString("val"), s);
	}

	private JSONObject convertObject(RDFNode object,
			Map<String, Integer> bNodeReference,
			Map<String, String> uriReference) {
		if(object.isAnon()) { //blank node
			return registerBlankNode(object.toString(),bNodeReference);
		} else if(object.isLiteral()) {
			return resolveLiteral(object.asLiteral(),uriReference);
		}
		else {
			return resolvePrefix(object.toString(),uriReference);
		}
	}

	private JSONObject resolveLiteral(Literal literal,
			Map<String, String> uriReference) {
		JSONObject node = new JSONObject();
		if(literal.getDatatypeURI().equals(Prefixes.Common.S2SLiteralMap)) {
			node.put("type", "binding");
			String[] parts = literal.getString().split(".");
			if(parts.length>1) {
				node.put("table", parts[0]);
				node.put("val", parts[1]);
			}
			else {
				node.put("val", literal.getString());
			}
		} else {
			node.put("type", "literal");
			node.put("val", literal.getString());
			node.put("datatype", resolvePrefix(literal.getDatatypeURI(),uriReference));
		}
		return node;
	}

	private JSONObject convertPredicate(Property predicate, Map<String, String> uriReference) {
		return resolvePrefix(predicate.toString(),uriReference);
	}

	private JSONObject convertSubject(Resource subject,
			Map<String, Integer> bNodeReference,
			Map<String, String> uriReference) {
		if(subject.isAnon()) { //blank node
			return registerBlankNode(subject.toString(),bNodeReference);
		} else {
			return resolvePrefix(subject.toString(),uriReference);
		}
	}

	private JSONObject resolvePrefix(String string, Map<String, String> uriReference) {
		JSONObject node = new JSONObject(); 
		node.put("type", "uri");
		node.put("raw", string);
		// extract prefix
		String[] parts = string.split("#");
		if(parts.length>1) {
			String prefix = uriReference.get(parts[0]);
			if(prefix!=null) {
				return node.put("val",prefix + ":" + parts[1]);
			} else {
				prefix = "p" + uriReference.size();
				uriReference.put(parts[0], prefix);
				return node.put("val",prefix + ":" + parts[1]);
			}
		}
		return node.put("val",string);
	}

	private JSONObject registerBlankNode(String string,
			Map<String, Integer> bNodeReference) {
		JSONObject node = new JSONObject(); 
		node.put("type", "bnode");
		Integer ref = bNodeReference.get(string);
		if(ref!=null) {
			return node.put("val", "_:b"+ref);
		} else {
			int size = bNodeReference.size();
			bNodeReference.put(string, size);
			return node.put("val", "_:b"+size);
		}
	}
	
	
}
