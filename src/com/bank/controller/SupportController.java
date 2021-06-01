package com.bank.controller;

import java.util.Scanner;

import com.bank.service.SupportService;

public class SupportController {

	Scanner scan = new Scanner(System.in);
	SupportService supportservice = new SupportService();

	public void login() {

		System.out.println("Enter username");
		String username = scan.nextLine();
		System.out.println("Enter password");
		String pass = scan.nextLine();
		supportservice.login(username, pass);

	}

	public void connect() {
		supportservice.connect();
	}
}
