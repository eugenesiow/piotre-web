package uk.ac.soton.ldanalytics.piotre.server.app;

import java.util.UUID;

public class App {
	UUID id;
	String name;
    String author;
    String description;
    String uri;
	public App(UUID id, String name, String author, String description,
			String uri) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.description = description;
		this.uri = uri;
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
	public String getUri() {
		return uri;
	}
    
}
