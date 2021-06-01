package com.bank.repository;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.bank.domain.Account;

public class AccountRepository {

	private Connection con;
	
	public AccountRepository() {
		con = ConnectDatabase.getConnection();
	}

	
	public int insertshedule(String from, String to, double amt, Timestamp ts) {
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("INSERT INTO schedule VALUES(?,?,?,?)");
			st.setString(1, from);
			st.setString(2, to);
			st.setDouble(3, amt);
			st.setTimestamp(4, ts);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	public int insert(Account obj)  {

		PreparedStatement st;
		try {
			
			st = con.prepareStatement("INSERT INTO account VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
			st.setString(1, obj.username);
			st.setString(2, obj.pass);
			st.setString(3, obj.email);
			st.setDouble(4, obj.amount);
			st.setInt(5, 0);
			st.setString(6, obj.pin);
			st.setTimestamp(7, obj.last_logged);
			st.setBoolean(8, false);
			st.setString(9, null);
			st.setString(10, null);
			st.setString(11, null);
			st.setTimestamp(12, obj.last_changed);
			return st.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Database error");
			return 0;
		}
		
	}

	public Timestamp getLast(String username)  {

		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT last_logged FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			rs.next();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public int updateLastLogged(String username, Timestamp ts)  {

		PreparedStatement st;
		try {
			st = con.prepareStatement("UPDATE account SET last_logged=? WHERE username=?");
			st.setTimestamp(1,ts);
			st.setString(2, username);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Database error");
			return 0;
//			e.printStackTrace();
		}
		
	}
	public Account getUser(String username)  {

		PreparedStatement st;
		ResultSet rs;
		Account acc = null;
		try {
			st = con.prepareStatement("SELECT username, pass, block FROM account WHERE username=?");
			st.setString(1, username);
			rs = st.executeQuery();
			
			if(rs.next()) {
				acc = new Account();
				acc.username = rs.getString(1);
				acc.pass = rs.getString(2);
			}
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return acc;
		}
		return acc;
	}
	
	public int updateBalance(String username, double amt)  {

		
		try {
			PreparedStatement st = con.prepareStatement("UPDATE account SET amount=? WHERE username=?");
			st.setDouble(1, amt);
			st.setString(2, username);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	
	public boolean available(String username) {
		
		try {
			PreparedStatement st = con.prepareStatement("SELECT * FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Database Error");
			e.printStackTrace();
			return false;
		}
		
	}
	
	public Double getBalance(String username) {
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT amount FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			rs.next();
			return rs.getDouble(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public boolean addPin(String username, String pin) {
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("UPDATE account SET pin=? WHERE username=?");
			st.setString(1, pin);
			st.setString(2, username);
			if(st.executeUpdate() == 1)
				return true;
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		}
		
	}
	public String getPin(String username) {
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT pin FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return rs.getString(1);				
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return "";
	}
	
	public int getPinCount(String username) {
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT pin_count FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);				
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return -1;
	}
	
	public int updatePinCount(String username, int count) {
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("UPDATE account SET pin_count=? WHERE username=?");
			st.setInt(1, count);
			st.setString(2, username);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return 0;
//			e.printStackTrace();
		}
		
	}
	
	public int block(String username, boolean val) {
		PreparedStatement st;
		try {
			st = con.prepareStatement("UPDATE account SET block=? WHERE username=?");
			st.setBoolean(1, val);
			st.setString(2, username);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return 0;
		}
		
	}
	public int getBlock(String username) {
		
		PreparedStatement st;
		try {
			
			st = con.prepareCall("SELECT block FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			rs.next();
			boolean res = rs.getBoolean(1);
			if(res)
				return 1;
			return 0;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			
			return -1;
		}
		
	}
	
	public String getEmail(String username) {
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT email FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Email not available");
			e.printStackTrace();
			return "";
		}
		
	}
	public List<String> getPass(String username){
		List<String> list = new ArrayList<String>();
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT pass, pass1, pass2, pass3 FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			rs.next();
			list.add(rs.getString(1));
			list.add(rs.getString(2));
			list.add(rs.getString(3));
			list.add(rs.getString(4));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return list;
		
	}
	
	public int updatePass(String username, String pass, String pass1, String pass2, String pass3, Timestamp ts) {
		
		PreparedStatement st;
		try {
			st = con.prepareStatement("UPDATE account SET pass=?, pass1=?, pass2=?, pass3=?, last_changed=? WHERE username=?");
			st.setString(1, pass);
			st.setString(2, pass1);
			st.setString(3, pass2);
			st.setString(4, pass3);
			st.setTimestamp(5, ts);
			st.setString(6, username);
			return st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	
	public Timestamp getLastChanged(String username) {
		
		PreparedStatement st;
		try {
			
			st = con.prepareStatement("SELECT last_changed FROM account WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			rs.next();
			return rs.getTimestamp(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}
		
	}
	
	public ArrayList<String> getBlocked(){
		
		ArrayList<String> list = new ArrayList<String>();
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT username FROM account WHERE block=?");
			st.setBoolean(1, true);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean transfer(String from, String to, Double amt) {
		
		try {
			con.setAutoCommit(false);
			PreparedStatement st = con.prepareStatement("UPDATE account SET amount=? WHERE username=?");
			Double bal = getBalance(from);
			st.setDouble(1, bal - amt);
			st.setString(2, from);
			st.executeUpdate();
			PreparedStatement st2 = con.prepareStatement("UPDATE account SET amount=? WHERE username=?");
			bal = getBalance(to);
			st.setDouble(1, bal + amt);
			st.setString(2, to);
			st.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				con.rollback();
				con.setAutoCommit(true);
				return false;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
			
		}
		
		
	}
	
//	public void unblock(String username) {
//		PreparedStatement st;
//		try {
//			st = con.prepareStatement("UPDATE account SET block=? WHERE username=?");
//			st.setBoolean(1, false);
//			st.setString(2, username);
//			st.executeUpdate();
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}
//		
//	}
	
}
