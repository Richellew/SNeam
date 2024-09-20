package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.SNeam_DatabaseManager;

public class SNeam_DatabaseManager {
	
	private final String USERNAME = "root"; //-> Default Settings
	private final String PASSWORD = "";
	private final String DATABASE = "revisiansneam"; //-> Sesuain sama nama database yg dipake dari XAMPP
	private final String HOST = "localhost:3306";
	private final String URL = String.format
			("jdbc:mysql://%s/%s", HOST, DATABASE);

	private Connection con;
	private Statement st; //-> untuk menjalani querynya
	private ResultSet rs;
	
	public static SNeam_DatabaseManager connect;

public static SNeam_DatabaseManager getInstance () {
		
		if (connect == null) {
			connect = new SNeam_DatabaseManager();
		}
		
		return connect;
	}
	public ResultSet execQuery (String query) {
		try {
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	public ResultSet getCartData(String userID) throws SQLException {
        String query = String.format("SELECT * FROM cart WHERE UserID = '%s'", userID);
        return execQuery(query);
    }
	public void execUpdate (String query) {
		
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private SNeam_DatabaseManager() {	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			st = con.createStatement();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}	

	public static PreparedStatement prepareStatement(String query) throws SQLException {
		PreparedStatement ps = null; 
		ps = connect.prepareStatement(query);
		
		return ps; 
		
	}
	
}