package uk.ac.soton.ldanalytics.piotre.server.metadata;

import java.util.UUID;

public class MetadataItem {
	private UUID itemId;
	private String name;
	private String data;
	public UUID getItemId() {
		return itemId;
	}
	public void setItemId(UUID itemId) {
		this.itemId = itemId;
	}
	public String getName() {
		return name;
	}
	public MetadataItem(UUID itemId, String name, String data) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.data = data;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
