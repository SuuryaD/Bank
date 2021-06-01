package com.bank.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bank.domain.Transaction;

public class TransactionRepository {

	private Connection con;
	public TransactionRepository() {
		con = ConnectDatabase.conn;
	}
	public void add(Transaction t) {
		
		try {
			
			PreparedStatement st = con.prepareStatement("INSERT INTO transaction VALUES (?,?,?,?)");
			st.setString(1,t.username);
			st.setString(2, t.description);
			st.setDouble(3, t.amount);
			st.setDate(4, t.date);
			st.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Database error");
			e.printStackTrace();
		}
		
	}
	
	public List<Transaction> read(String username, Date start, Date end){
		
		List<Transaction> list = new ArrayList<Transaction>();
		
		try {
			PreparedStatement st = con.prepareStatement("SELECT * FROM transaction WHERE date > ? AND date < ? AND username=? ");
			st.setDate(1, start);
			st.setDate(2, end);
			st.setString(3, username);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Transaction t = new Transaction();
				t.username = rs.getString(1);
				t.description = rs.getString(2);
				t.amount = rs.getDouble(3);
				t.date = rs.getDate(4);
				list.add(t);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	
}
