package uk.ac.soton.ldanalytics.piotre.server.mapping;

import java.util.UUID;

public class Mapping {
	UUID id;
	String name;
    String author;
    String uri;
    String content;
    String format;
    String dataRelName;
    String dataRelId;
	public Mapping(UUID id, String name, String author, String uri) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.uri = uri;
	}
	public Mapping(UUID id, String name, String author, String uri,
			String content, String format) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.uri = uri;
		this.content = content;
		this.format = format;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public void setDataRelName(String dataRelName) {
		this.dataRelName = dataRelName;
	}
	public void setDataRelId(String dataRelId) {
		this.dataRelId = dataRelId;
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
	public String getUri() {
		return uri;
	}
	public String getDataRelName() {
		return dataRelName;
	}
	public String getDataRelId() {
		return dataRelId;
	}
}
