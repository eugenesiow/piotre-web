package uk.ac.soton.ldanalytics.piotre.server.data;

public class Data {
	public enum DataType {
	    STORE, HISTORICAL, STREAM 
	}
	
	String id;
	String name;
    String author;
    DataType type;
	
	public Data(String id, String name, String author, DataType type) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public DataType getType() {
		return type;
	}
}

