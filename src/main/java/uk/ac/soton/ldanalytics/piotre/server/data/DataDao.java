package uk.ac.soton.ldanalytics.piotre.server.data;

import java.util.List;
import java.util.UUID;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import uk.ac.soton.ldanalytics.piotre.server.data.Data.DataType;

import com.google.common.collect.ImmutableList;

public class DataDao {
	private Sql2o sql2o;
	
	public DataDao(Sql2o sql2o) {
		this.sql2o = sql2o;
	}
	
	public Iterable<Data> getAllData() {
		try (Connection conn = sql2o.open()) {
            List<Data> data = conn.createQuery("select * from data")
                    .executeAndFetch(Data.class);
            return data;
        }
    }
}
