package com.bank.service;

import com.bank.domain.Support;
import com.bank.repository.SupportRepository;

public class SupportService {

	SupportRepository supportdata = new SupportRepository();

	public void login(String username, String pass) {

		Support s = supportdata.getByUsername(username);
		if (s == null)
			System.out.println("Username or password is incorrect");
		else if (s.pass.equals(pass)) {
			while (true) {
				if (updateStatus(username, true) == 1)
					break;
			}

			new Server().run(s.port);

			while (true) {
				if (updateStatus(username, false) == 1)
					break;
			}

			return;
		}

		System.out.println("Username or password is incorrect");

	}

	public int updateStatus(String username, boolean status) {
		return supportdata.updateStatus(username, status);
	}

	public void connect() {

		Support s = supportdata.getAvailable();

		if (s == null) {
			System.out.println("No support staff available");
			return;
		}
		while (true) {
			if (updateStatus(s.username, false) == 1)
				break;
		}

		new Client().run(s.port);

		while (true) {
			if (updateStatus(s.username, true) == 1)
				break;
		}

	}

}
