package uk.ac.soton.ldanalytics.piotre.server.user;

public class User {
    public String getUsername() {
		return username;
	}
	public String getSalt() {
		return salt;
	}
	public String getHashedPassword() {
		return hashedPassword;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public User(String username, String salt, String hashedPassword, String apiKey) {
		super();
		this.username = username;
		this.salt = salt;
		this.hashedPassword = hashedPassword;
		this.apiKey = apiKey;
	}
	String username;
    String salt;
    String hashedPassword;
    String apiKey;
}
