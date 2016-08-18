package uk.ac.soton.ldanalytics.piotre.server.mapping;

import java.util.UUID;

public class Mapping {
	UUID id;
	String name;
    String author;
    String uri;
    String content;
    String format;
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
}
