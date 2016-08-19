package uk.ac.soton.ldanalytics.piotre.server.relation;

import java.util.UUID;

public class Relation {
	public Relation(UUID id1, UUID id2) {
		super();
		this.id1 = id1;
		this.id2 = id2;
	}
	public UUID getId1() {
		return id1;
	}
	public UUID getId2() {
		return id2;
	}
	UUID id1;
	UUID id2;
}
