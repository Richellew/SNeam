package modul;

public abstract class Models_User {
	private String userID;
	private String username;
	private String email;
	private String password;
	private int phoneNumber;
	private String role;

public Models_User(String userID, String username, String email, String password, int phoneNumber, String role) {
		super();
		this.userID = userID;
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

public String getUserID() {
	return userID;
}

public void setUserID(String userID) {
	this.userID = userID;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public int getPhoneNumber() {
	return phoneNumber;
}

public void setPhoneNumber(int phoneNumber) {
	this.phoneNumber = phoneNumber;
}

public String getRole() {
	return role;
}

public void setRole(String role) {
	this.role = role;
}


}