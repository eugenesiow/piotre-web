package uk.ac.soton.ldanalytics.piotre.server.data;

import java.util.UUID;

public class Data {
	public enum DataType {
	    STORE, HISTORICAL, STREAM 
	}
	
	UUID id;
	String name;
    String author;
    String description;
    DataType type;
	
	public Data(UUID id, String name, String author, String description, DataType type) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.description = description;
		this.type = type;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return description;
	}

	public DataType getType() {
		return type;
	}
}

