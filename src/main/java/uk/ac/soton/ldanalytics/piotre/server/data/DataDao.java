package uk.ac.soton.ldanalytics.piotre.server.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import uk.ac.soton.ldanalytics.piotre.server.data.Data.DataType;
import uk.ac.soton.ldanalytics.piotre.server.mapping.Mapping;
import uk.ac.soton.ldanalytics.piotre.server.metadata.MetadataItem;
import uk.ac.soton.ldanalytics.piotre.server.metadata.SchemaItem;

public class DataDao {
	private Sql2o sql2o;
	
	public DataDao(Sql2o sql2o) {
		this.sql2o = sql2o;
	}
	
	public Iterable<SchemaItem> getAllSchema(String category, String type) {
		try (Connection conn = sql2o.open()) {
            List<SchemaItem> schema = conn.createQuery("select * from metadata_schema where category='"+category+"' AND type='"+type+"' ORDER BY sortorder")
                    .executeAndFetch(SchemaItem.class);
            return schema;
        }
    }
	
	public Iterable<Data> getAllData() {
		try (Connection conn = sql2o.open()) {
            List<Data> data = conn.createQuery("select * from data")
                    .executeAndFetch(Data.class);
            return data;
        }
    }
	
	public Data getDatum(String id) {
		try (Connection conn = sql2o.open()) {
			List<Data> data = conn.createQuery("select * from data where id='"+id+"'")
	                .executeAndFetch(Data.class);
	        return data.get(0);
		}
	}
	
	public  Iterable<Schema> getAllStreamSchema() {
		try (Connection conn = sql2o.open()) {
            List<Schema> schema = conn.createQuery("select t2.name, t2.id, t1.id2 AS dataRelId, t2.content, t3.data AS dataStreamUri from rel_schema_data t1, schema t2, metadata t3 where t2.id=t1.id1 AND t1.id2=t3.itemId AND t3.name='stream_uri'")
            		.executeAndFetch(Schema.class);
            return schema;
        }
	}
	
	public  Iterable<Mapping> getAllMappings() {
		try (Connection conn = sql2o.open()) {
            List<Mapping> mappings = conn.createQuery("select t2.name, t2.id, t1.id2 AS dataRelId from rel_mappings_data t1, mappings t2 where t2.id=t1.id1")
                    .executeAndFetch(Mapping.class);
            return mappings;
        }
	}
	
	public  Iterable<Mapping> getMappingsByDatum(String id) {
		try (Connection conn = sql2o.open()) {
            List<Mapping> mappings = conn.createQuery("select t2.name, t2.author, t2.id, t2.uri from rel_mappings_data t1, mappings t2 where t1.id2='"+id+"' AND t2.id=t1.id1")
                    .executeAndFetch(Mapping.class);
            return mappings;
        }
	}
	
	public Map<String,String> getMetadata(String id) {
		try (Connection conn = sql2o.open()) {
			Map<String,String> metadata = new HashMap<String,String>();
			List<Map<String,Object>> results = conn.createQuery("select * from metadata where itemId='"+id+"'")
					.executeAndFetchTable().asList();
			for(Map<String,Object> result:results) {
				metadata.put(result.get("name").toString(), result.get("data").toString());
			}
			return metadata;
	    }
	}
	
	public boolean addData(String type,String name, String author, String description, Map<String,String> metadata) {
		UUID id = UUID.randomUUID();
		try (Connection conn = sql2o.open()) {
			conn.createQuery("insert into data(id, name, author, description, type) VALUES (:id, :name, :author, :description, :type)")
				.bind(new Data(id,name,author,description,DataType.valueOf(type.toUpperCase())))
				.executeUpdate();
			for(Entry<String,String> item:metadata.entrySet()) {
				conn.createQuery("insert into metadata(itemId, name, data) VALUES (:itemId, :name, :data)")
					.bind(new MetadataItem(id,item.getKey(),item.getValue()))
					.executeUpdate();
			}
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean updateData(String strId, String type,String name, String author, String description, Map<String,String> metadata) {
		UUID id = UUID.fromString(strId);
		try (Connection conn = sql2o.open()) {
			conn.createQuery("update data set name=:name,author=:author,description=:description,type=:type where id=:id")
				.bind(new Data(id,name,author,description,DataType.valueOf(type.toUpperCase())))
				.executeUpdate();
			for(Entry<String,String> item:metadata.entrySet()) {
				conn.createQuery("update metadata set data=:data where itemId=:itemId AND name=:name")
					.bind(new MetadataItem(id,item.getKey(),item.getValue()))
					.executeUpdate();
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
