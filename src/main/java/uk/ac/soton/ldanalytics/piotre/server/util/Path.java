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
		public static String getACCOUNT() {
			return ACCOUNT;
		}
		public static String getDATA() {
			return DATA;
		}
		public static final String INDEX = "/index/";
        public static final String LOGIN = "/login/";
        public static final String LOGOUT = "/logout/";
        public static final String ACCOUNT = "/account/";
        public static final String BOOKS = "/books/";
        public static final String DATA = "/data/";
        public static final String ONE_BOOK = "/books/:isbn/";
    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String BOOKS_ALL = "/velocity/book/all.vm";
        public static final String BOOKS_ONE = "/velocity/book/one.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
        public final static String DATA = "/velocity/data/data.vm";
    }
    
    public static class PageNames {
        public final static String INDEX = "Getting Started";
        public final static String DATA = "Data";
        public final static String LOGIN = "Login";
        public static final String BOOKS_ALL = "Books";
        public static String getIndex() {
			return INDEX;
		}
		public static String getData() {
			return DATA;
		}
		public static String getLogin() {
			return LOGIN;
		}
		public static String getBooksAll() {
			return BOOKS_ALL;
		}
		public static String getBooksOne() {
			return BOOKS_ONE;
		}
		public static String getNotFound() {
			return NOT_FOUND;
		}
		public static final String BOOKS_ONE = "Book";
        public static final String NOT_FOUND = "Not Found";
    }

}
