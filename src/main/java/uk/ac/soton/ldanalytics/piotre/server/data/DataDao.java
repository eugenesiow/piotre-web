package uk.ac.soton.ldanalytics.piotre.server.data;

import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import uk.ac.soton.ldanalytics.piotre.server.metadata.SchemaItem;

public class DataDao {
	private Sql2o sql2o;
	
	public DataDao(Sql2o sql2o) {
		this.sql2o = sql2o;
	}
	
	public Iterable<SchemaItem> getAllSchema(String category, String type) {
		try (Connection conn = sql2o.open()) {
            List<SchemaItem> schema = conn.createQuery("select * from metadata_schema where category='"+category+"' AND type='"+type+"'")
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
}
