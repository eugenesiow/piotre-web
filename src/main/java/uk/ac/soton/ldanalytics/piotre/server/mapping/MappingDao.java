package uk.ac.soton.ldanalytics.piotre.server.mapping;

import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class MappingDao {
	private Sql2o sql2o;
	
	public MappingDao(Sql2o sql2o) {
		this.sql2o = sql2o;
	}
	
	public Iterable<Mapping> getAllMappings() {
		try (Connection conn = sql2o.open()) {
            List<Mapping> mappings = conn.createQuery("select t1.id, t1.name, t1.author, t1.uri, t2.id2 as dataRelId, t3.name as dataRelName from mappings t1, rel_mappings_data t2, data t3 where t1.id=t2.id1 AND t2.id2=t3.id")
                    .executeAndFetch(Mapping.class);
            return mappings;
        }
    }
}
