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
	public User(String username, String salt, String hashedPassword) {
		super();
		this.username = username;
		this.salt = salt;
		this.hashedPassword = hashedPassword;
	}
	String username;
    String salt;
    String hashedPassword;
}
