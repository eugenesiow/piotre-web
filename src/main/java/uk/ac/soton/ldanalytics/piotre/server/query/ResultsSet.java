package uk.ac.soton.ldanalytics.piotre.server.query;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ResultsSet {
	List<Map<String,Object>> resultsData;
	Set<String> header = new TreeSet<String>();
	String error = null;
	
	public ResultsSet(String error) {
		this.error = error;
	}
	
	public ResultsSet(List<Map<String,Object>> results) {
		resultsData = results;
		for(Map<String,Object> result:results) {
			header.addAll(result.keySet());
		}
	}
	
	public Set<String> getHeader() {
		return header;
	}
	
	public List<Map<String,Object>> getResults() {
		return resultsData;
	}
	
	public String getErrorMessage() {
		return error;
	}
}
