package com.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bank.domain.Request;

public class RequestRepository {

	private Connection con = ConnectDatabase.getConnection();
	
	public void insert(Request r) {
		try {
			PreparedStatement st = con.prepareStatement("INSERT INTO loan_request VALUES(?,?,?,?,?)");
			st.setInt(1, r.loan_no);
			st.setString(2, r.username);
			st.setDouble(3, r.amount);
			st.setString(4, r.description);
			st.setDate(5, r.requested_date);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int checkLoanNo(int loan_no) {
		
		try {
			PreparedStatement st = con.prepareStatement("SELECT loan_no FROM loan_request WHERE loan_no=?");
			st.setInt(1, loan_no);
			ResultSet rs = st.executeQuery();
			if(rs.next())
				return 1;
			return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<Request> getAll(String username){
		
		List<Request> list = new ArrayList<Request>();
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT * FROM loan_request WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Request r = new Request();
				r.loan_no = rs.getInt(1);
				r.username = rs.getString(2);
				r.amount = rs.getDouble(3);
				r.description = rs.getString(4);
				r.requested_date = rs.getDate(5);
				list.add(r);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public List<Request> getAll(){
		
		List<Request> list = new ArrayList<Request>();
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT * FROM loan_request");
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Request r = new Request();
				r.loan_no = rs.getInt(1);
				r.username = rs.getString(2);
				r.amount = rs.getDouble(3);
				r.description = rs.getString(4);
				r.requested_date = rs.getDate(5);
				list.add(r);
			}
			
		} catch (SQLException e) {
//			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public int cancel(int loan_no) {
		
		try {
			PreparedStatement st = con.prepareStatement("DELETE FROM loan_request WHERE loan_no=?");
			st.setInt(1, loan_no);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}

}
