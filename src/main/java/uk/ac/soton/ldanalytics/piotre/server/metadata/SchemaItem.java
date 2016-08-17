package uk.ac.soton.ldanalytics.piotre.server.metadata;

import org.json.JSONObject;

public class SchemaItem {
	private String category;
	private String name;
	private String type; 
	private int sortorder;
	private String data;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public SchemaItem(String category, String name, String type, int order,
			String data) {
		super();
		this.category = category;
		this.name = name;
		this.type = type;
		this.sortorder = order;
		this.data = data;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSortorder() {
		return sortorder;
	}
	public void setSortorder(int order) {
		this.sortorder = order;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public JSONObject decodeJson() {
		return new JSONObject(data);
	}
}
