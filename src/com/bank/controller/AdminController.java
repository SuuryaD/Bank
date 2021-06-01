package com.bank.controller;

import java.util.Scanner;

import com.bank.service.AdminService;

public class AdminController {
	
	AdminService adminservice = new AdminService();
	Scanner scan = new Scanner(System.in);
	
	public void login() {
		
		System.out.println("Enter username");
		String username = scan.nextLine();
		System.out.println("Enter password");
		String pass = scan.nextLine();
		
		if(adminservice.login(username,pass)) {
			menu();
		}
		
		System.out.println("Username or password incorrect");
		
	}
	
	public void menu() {
		
		do {
			System.out.println("MENU");
			System.out.println("1. Block Accounts");
			System.out.println("2. Unblock Accounts");
			System.out.println("3. Process Loans");
			System.out.println("0. Exit");
			System.out.println("Enter a option");
			String ch = scan.nextLine();
			
			switch(ch) {
			case "0":
				return;
			case "1":
				block();
				break;
			case "2":
				unblock();
				break;
			case "3":
				adminservice.process();
				
				break;
			default:
				System.out.println("Enter a valid option");
				break;
			}
			
		}while(true);
		
	}
	
	private void block() {
		
		System.out.println("Enter the username to block");
		String username = scan.nextLine();
		adminservice.block(username);
		
	}
	private void unblock() {
		
		do {
			System.out.println("1. View All");
			System.out.println("2. Search");
			System.out.println("0. Back");
			String ch = scan.nextLine();
			switch(ch) {
			
			case "0":
				return;
			case "1":
				adminservice.unblock();
				break;
			case "2":
				System.out.println("Enter username");
				String username = scan.nextLine();
				adminservice.unblock2(username);
				
				break;
			default:
				System.out.println("Enter a valid option");
				break;
			}
			
		}while(true);
		
		
	}
	
	
	
	
}
