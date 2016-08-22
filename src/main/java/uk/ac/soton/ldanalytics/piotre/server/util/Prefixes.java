package uk.ac.soton.ldanalytics.piotre.server.util;

import java.util.HashMap;
import java.util.Map;

public class Prefixes {
	public static class Common {
		public static final String S2SLiteralMap = "http://iot.soton.ac.uk/s2s/s2sml#literalMap";
		public static Map<String,String> getMap() {
			Map<String,String> prefixes = new HashMap<String,String>();
			prefixes.put("http://www.w3.org/1999/02/22-rdf-syntax-ns", "rdf");
			prefixes.put("http://www.w3.org/2000/01/rdf-schema", "rdfs");
			prefixes.put("http://www.w3.org/2006/time", "owl-time");
			prefixes.put("http://purl.oclc.org/NET/ssnx/ssn", "ssn");
			prefixes.put("http://www.w3.org/2001/XMLSchema#", "xsd");
			prefixes.put("http://purl.oclc.org/NET/iot", "iot");
			return prefixes;
		}
	}
}
