package com.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bank.domain.Support;

public class SupportRepository {

	private Connection con;

	public SupportRepository() {
		con = ConnectDatabase.getConnection();
	}

	public Support getByUsername(String username) {

		Support s = null;
		try {
			PreparedStatement st = con.prepareStatement("SELECT * FROM support WHERE username=?");
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				s = new Support();
				s.username = rs.getString(1);
				s.pass = rs.getString(2);
				s.port = rs.getInt(3);
				s.status = rs.getBoolean(4);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;

	}

	public int updateStatus(String username, boolean status) {

		try {
			PreparedStatement st = con.prepareStatement("UPDATE support SET status=? WHERE username=?");
			st.setBoolean(1, status);
			st.setString(2, username);
			return st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Support getAvailable() {

		Support s = new Support();
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT username , port FROM support WHERE status=? LIMIT 1");
			st.setBoolean(1, true);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
//				s = new Support();
				s.username = rs.getString(1);
				s.port = rs.getInt(2);
			} else
				return null;

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return s;

	}

}
