package uk.ac.soton.ldanalytics.piotre.server.util;

public class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web {
        public static String getINDEX() {
			return INDEX;
		}
		public static String getLOGIN() {
			return LOGIN;
		}
		public static String getLOGOUT() {
			return LOGOUT;
		}
		public static String getAPPS() {
			return APPS;
		}
		public static String getAPPS_ADD() {
			return APPS_ADD;
		}
		public static String getACCOUNT() {
			return ACCOUNT;
		}
		public static String getDATA() {
			return DATA;
		}
		public static String getDATA_ADD() {
			return DATA_ADD;
		}
		public static String getDATUM() {
			return DATUM;
		}
		public static String getMAPPING() {
			return MAPPING;
		}
		public static String getMAPPINGS_ADD() {
			return MAPPINGS_ADD;
		}
		public static String getMAPPING_TRANSLATE() {
			return MAPPING_TRANSLATE;
		}
		public static String getMAPPING_SAVE() {
			return MAPPING_SAVE;
		}
		public static String getMETADATA() {
			return METADATA;
		}
		public static String getMAPPINGS() {
			return MAPPINGS;
		}
		public static String getQUERY() {
			return QUERY_STORE;
		}
		public static String getQUERY_STREAM() {
			return QUERY_STREAM;
		}
		public static final String INDEX = "/index/";
        public static final String LOGIN = "/login/";
        public static final String LOGOUT = "/logout/";
        public static final String ACCOUNT = "/account/";
        public static final String DATA = "/data/";
        public static final String DATA_ADD = "/data/add/:type/";
        public static final String DATUM = "/data/:id/";
        public static final String METADATA = "/metadata/";
        public static final String APPS = "/apps/";
        public static final String APPS_ADD = "/apps/add/";
        public static final String APP = "/apps/:id/";
        public static final String MAPPINGS = "/mappings/";
        public static final String MAPPINGS_ADD = "/mappings/add/";
        public static final String MAPPING = "/mappings/:id/";
        public static final String MAPPING_TRANSLATE = "/mappings/translate/";
        public static final String MAPPING_SAVE = "/mappings/save/";
        public static final String QUERY_STORE = "/query/";
        public static final String QUERY_STREAM = "/query_stream/";
    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String BOOKS_ALL = "/velocity/book/all.vm";
        public static final String BOOKS_ONE = "/velocity/book/one.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
        public final static String DATA = "/velocity/data/data.vm";
        public final static String DATA_ADD = "/velocity/data/add.vm";
        public final static String DATUM = "/velocity/data/datum.vm";
        public final static String APPS = "/velocity/apps/apps.vm";
        public final static String APPS_ADD = "/velocity/apps/add.vm";
        public final static String APP = "/velocity/apps/app.vm";
        public final static String MAPPING = "/velocity/mappings/mapping.vm";
        public final static String MAPPINGS_ADD = "/velocity/mappings/add.vm";
        public final static String QUERY_STORE = "/velocity/query/sparql.vm";
        public final static String QUERY_STREAM = "/velocity/query/stream.vm";
        public final static String MAPPINGS = "/velocity/mappings/mappings.vm";
    }
    
    public static class PageNames {
        public final static String INDEX = "Getting Started";
        public final static String DATA = "Data";
        public final static String METADATA = "Metadata";
        public final static String APPS = "Applications";
        public final static String MAPPINGS = "Mappings";
        public final static String LOGIN = "Login";
		public final static String QUERY_STORE = "SPARQL";
        public final static String QUERY_STREAM = "Stream Queries";
        public static final String NOT_FOUND = "Not Found";
        public static String getIndex() {
			return INDEX;
		}
		public static String getData() {
			return DATA;
		}
		public static String getApps() {
			return APPS;
		}
		public static String getMappings() {
			return MAPPINGS;
		}
		public static String getMetadata() {
			return METADATA;
		}
		public static String getLogin() {
			return LOGIN;
		}
		public static String getNotFound() {
			return NOT_FOUND;
		}
		public static String getQuery() {
			return QUERY_STORE;
		}
		public static String getQueryStream() {
			return QUERY_STREAM;
		}
    }

}
