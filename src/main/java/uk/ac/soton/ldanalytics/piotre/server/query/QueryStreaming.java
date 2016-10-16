package uk.ac.soton.ldanalytics.piotre.server.query;

import java.util.UUID;

public class QueryStreaming {
	UUID id;
	String name;
    String author;
    UUID dataStream;
    String sparql;
    String epl;
	public QueryStreaming(UUID id, String name, String author, UUID dataStream,
			String sparql, String epl) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.dataStream = dataStream;
		this.sparql = sparql;
		this.epl = epl;
	}
	public QueryStreaming(UUID id, String name, String sparql) {
		this.id = id;
		this.name = name;
		this.sparql = sparql;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public UUID getDataStream() {
		return dataStream;
	}
	public void setDataStream(UUID dataStream) {
		this.dataStream = dataStream;
	}
	public String getSparql() {
		return sparql;
	}
	public void setSparql(String sparql) {
		this.sparql = sparql;
	}
	public String getEpl() {
		return epl;
	}
	public void setEpl(String epl) {
		this.epl = epl;
	}
	public UUID getId() {
		return id;
	}
}
