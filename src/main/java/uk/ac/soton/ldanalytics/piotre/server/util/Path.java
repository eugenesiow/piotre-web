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
		public static String getBOOKS() {
			return BOOKS;
		}
		public static String getONE_BOOK() {
			return ONE_BOOK;
		}
		public static final String INDEX = "/index/";
        public static final String LOGIN = "/login/";
        public static final String LOGOUT = "/logout/";
        public static final String BOOKS = "/books/";
        public static final String ONE_BOOK = "/books/:isbn/";
    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String BOOKS_ALL = "/velocity/book/all.vm";
        public static final String BOOKS_ONE = "/velocity/book/one.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
    }

}
