package uk.ac.soton.ldanalytics.piotre.server.data;

import java.util.List;
import java.util.UUID;

import uk.ac.soton.ldanalytics.piotre.server.data.Data.DataType;

import com.google.common.collect.ImmutableList;

public class DataDao {
	
	private final List<Data> data = ImmutableList.of(
            new Data(UUID.randomUUID().toString(), "Moby Dick", "Herman Melville", DataType.STORE),
            new Data(UUID.randomUUID().toString(), "A Christmas Carol", "Charles Dickens", DataType.STREAM)            
    );
	
	public Iterable<Data> getAllData() {
        return data;
    }
}
