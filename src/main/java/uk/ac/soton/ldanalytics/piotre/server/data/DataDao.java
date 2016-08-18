package uk.ac.soton.ldanalytics.piotre.server.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import uk.ac.soton.ldanalytics.piotre.server.data.Data.DataType;
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
