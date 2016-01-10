package me.JavaExcption.NetworkManager.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
	
	private static Connection connection;
	
	public static Connection connect(String host, int port, String database, String user, String password) {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
			System.out.println("Successfully connected to MySQL database!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void disconnect() {
		try {
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasConnection() {
		if(connection != null) {
			return true;
		}
		return false;
	}
}
