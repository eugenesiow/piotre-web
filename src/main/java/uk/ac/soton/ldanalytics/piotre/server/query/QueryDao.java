package uk.ac.soton.ldanalytics.piotre.server.query;

import static uk.ac.soton.ldanalytics.piotre.server.Application.mappingDao;

import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpWalker;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import uk.ac.soton.ldanalytics.piotre.server.mapping.Mapping;
import uk.ac.soton.ldanalytics.sparql2sql.model.RdfTableMapping;
import uk.ac.soton.ldanalytics.sparql2sql.model.RdfTableMappingJena;
import uk.ac.soton.ldanalytics.sparql2sql.model.SparqlOpVisitor;

public class QueryDao {
	private Sql2o sql2o;
	
	public QueryDao(Sql2o sql2o) {
		this.sql2o = sql2o;
	}
	
	public ResultsSet queryStore(Map<String,String> metadata, Iterable<Mapping> mappings, String queryStr) {	
		try {
			//load mappings to sparql2sql
			RdfTableMapping mappingStore = new RdfTableMappingJena();
			for(Mapping mapping:mappings) {
				String mappingContent = mappingDao.getMapping(mapping.getId().toString()).getContent();
				mappingStore.loadMappingStr(mappingContent);
			}
			
			//parse query to algebra
			Query query = QueryFactory.create(queryStr);
			Op op = Algebra.compile(query);
		
			//translate to SQL
			SparqlOpVisitor v = new SparqlOpVisitor();
			v.useMapping(mappingStore);
			OpWalker.walk(op,v);
			
			//DB connection
	    	Sql2o querySql2o = new Sql2o(metadata.get("jdbc_url"), metadata.get("username"), metadata.get("password"));
	    	try (Connection conn = querySql2o.open()) {
	    		return new ResultsSet(conn.createQuery(v.getSQL()).executeAndFetchTable().asList());
	        } catch(Exception e) {
	        	e.printStackTrace();
	        	return new ResultsSet(e.getMessage());
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
    }
}
