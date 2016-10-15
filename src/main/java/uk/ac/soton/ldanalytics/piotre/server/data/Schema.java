package uk.ac.soton.ldanalytics.piotre.server.data;

import java.util.UUID;

public class Schema {
	UUID id;
	String name;
    String author;
    String content;
    String dataStreamUri;
    String dataRelId;
	public Schema(UUID id, String name, String author) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
	}
	public Schema(UUID id, String name, String author, String content) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public void setDataStreamUri(String dataStreamUri) {
		this.dataStreamUri = dataStreamUri;
	}
	public void setDataRelId(String dataRelId) {
		this.dataRelId = dataRelId;
	}
	public String getDataStreamUri() {
		return dataStreamUri;
	}
	public String getDataRelId() {
		return dataRelId;
	}
}
