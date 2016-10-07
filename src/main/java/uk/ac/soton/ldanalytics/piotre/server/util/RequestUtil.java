package uk.ac.soton.ldanalytics.piotre.server.util;

import java.util.HashMap;
import java.util.Map;

import spark.Request;

public class RequestUtil {

    public static String getQueryLocale(Request request) {
        return request.queryParams("locale");
    }
    
    public static String getParamId(Request request) {
        return request.params("id");
    }
    
    public static String getQueryContent(Request request) {
        return request.queryParams("content");
    }
  
    public static String getParamDataType(Request request) {
        return request.params("type");
    }
    
    public static String getQueryDataType(Request request) {
        return request.queryParams("type");
    }
    
    public static String getQueryId(Request request) {
        return request.queryParams("id");
    }
    
    public static String getQueryQuery(Request request) {
        return request.queryParams("query");
    }
    
    public static String getQueryName(Request request) {
        return request.queryParams("name");
    }
    
    public static String getQueryUri(Request request) {
        return request.queryParams("uri");
    }
    
    public static String getQueryAuthor(Request request) {
        return request.queryParams("author");
    }
    
    public static String getQueryDescription(Request request) {
        return request.queryParams("description");
    }
    
    public static Map<String,String> getQueryMetadata(Request request) {
    	Map<String,String> map = new HashMap<String,String>();
    	for(String param:request.queryParams()) {
    		if(!param.equals("name")&&!param.equals("description")&&!param.equals("author")) {
    			map.put(param, request.queryParams(param));
    		}
    	}
    	return map;
    }

    public static String getQueryUsername(Request request) {
        return request.queryParams("username");
    }

    public static String getQueryPassword(Request request) {
        return request.queryParams("password");
    }

    public static String getQueryLoginRedirect(Request request) {
        return request.queryParams("loginRedirect");
    }

    public static String getSessionLocale(Request request) {
        return request.session().attribute("locale");
    }

    public static String getSessionCurrentUser(Request request) {
        return request.session().attribute("currentUser");
    }

    public static boolean removeSessionAttrLoggedOut(Request request) {
        Object loggedOut = request.session().attribute("loggedOut");
        request.session().removeAttribute("loggedOut");
        return loggedOut != null;
    }

    public static String removeSessionAttrLoginRedirect(Request request) {
        String loginRedirect = request.session().attribute("loginRedirect");
        request.session().removeAttribute("loginRedirect");
        return loginRedirect;
    }

    public static boolean clientAcceptsHtml(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("text/html");
    }

    public static boolean clientAcceptsJson(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("application/json");
    }

}
