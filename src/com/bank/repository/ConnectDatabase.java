package com.bank.repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDatabase {

	
	static Connection conn = null;
	public static Connection getConnection() {
		if (conn != null) return conn;
	      
		String database = "bank";
		String Username = "root";
		String password = "";
		return getConnection(database, Username, password);
	}
	private static Connection getConnection(String databaseName, String UserName, String password) {
	      try {
//	         Class.forName("com.mysql.cj.jdbc.Driver");
	         conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?useSSL=false", UserName, password);
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return conn;
}
}
