package uk.ac.soton.ldanalytics.piotre.server.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import uk.ac.soton.ldanalytics.piotre.server.query.ResultsSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOEXception while mapping object (" + data + ") to JSON");
        }
    }
    
    public static String resultSetToJson(ResultsSet results) {
    	JSONObject resultSet = new JSONObject();
    	if(results.getErrorMessage()!=null) {
    		resultSet.put("error", results.getErrorMessage());
    	} else {
	    	JSONArray header = new JSONArray();
	    	for(String resultHead:results.getHeader()) {
	    		header.put(resultHead);
	    	}
	    	resultSet.put("header", header);
	    	JSONArray resultsArr = new JSONArray();
	    	for(Map<String, Object> result:results.getResults()) {
	    		JSONObject resultObj = new JSONObject();
	    		for(String resultHead:results.getHeader()) {
	    			JSONObject resultVal = new JSONObject();
	    			resultVal.put("value",result.get(resultHead));
	    			resultObj.put(resultHead, resultVal);
	        	}
	    		resultsArr.put(resultObj);
	    	}
	    	JSONObject bindings = new JSONObject();
	    	bindings.put("bindings", resultsArr);
	    	resultSet.put("results", bindings);
    	}
		return resultSet.toString();
    }
}
