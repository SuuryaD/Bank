package com.bank.repository;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bank.domain.Loan;
import com.bank.domain.Request;

public class LoanRepository {


	private Connection conn = ConnectDatabase.getConnection();

	
	public void insert(Request r, Date due ) {
		
		PreparedStatement st;
		try {
			
			st = conn.prepareStatement("INSERT INTO loan VALUES (?,?,?,?,?,?)");
			st.setInt(1, r.loan_no);
			st.setString(2, r.username);
			st.setDouble(3, r.amount);
			st.setString(4, r.description);
			st.setDate(5, Date.valueOf(LocalDate.now()));
			st.setDate(6, due);
			st.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public List<Loan> getAll(String username){
		
		List<Loan> list = new ArrayList<Loan>();
		PreparedStatement st;
		try {
			st = conn.prepareStatement("SELECT * FROM loan WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				Loan l = new Loan();
				l.loan_no = rs.getInt(1);
				l.username = rs.getString(2);
				l.amount = rs.getDouble(3);
				l.description = rs.getString(4);
				l.sdate = rs.getDate(5);
				l.due_date = rs.getDate(6);
				list.add(l);
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		

		
	}
	
	public int cancel(int loan_no) {
		
		PreparedStatement st;
		try {
			st = conn.prepareStatement("DELETE FROM loan WHERE loan_no=?");
			st.setInt(1, loan_no);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return 0;
		}
		

		
	}
}
