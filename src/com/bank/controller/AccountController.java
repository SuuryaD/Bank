package com.bank.controller;

import java.sql.Timestamp;
import java.util.Scanner;

import com.bank.service.AccountService;
import com.bank.service.RequestService;
import com.bank.service.TransactionService;

public class AccountController {

	private Scanner scan = new Scanner(System.in);
	private AccountService accountservice;
	private TransactionService tservice;
	private	RequestService rservice;
	public AccountController() {

		accountservice = new AccountService();
		tservice = new TransactionService();
		rservice = new RequestService();
	}

	public void drive() {

		do {

			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("0. Back");
			String ch = scan.nextLine();

			switch (ch) {

			case "0":
				return;
			case "1":
				login();
				break;
			case "2":
				register();
				break;

			default:
				System.out.println("Enter a valid option");
				break;
			}

		} while (true);

	}

	private void login() {

		System.out.println("Enter user name:");
		String username = scan.nextLine();
		System.out.println("Enter Password:");
		String pass = scan.nextLine();

		if (accountservice.login(username, pass)) {

			int rs = accountservice.lastChanged(username);
			if (rs == -1) {
				System.out.println("Something went wrong. Try again!");
				return;
			} else if (rs == 1) {
				if (!changePass(username)) {
					return;
				}
			}
			login(username);

		}
	}

	private void register() {

		String username, pass1, pass2, email;
		System.out.println("Enter 'exit' to exit registration");
		do {
			System.out.println("Enter User Name");
			username = scan.nextLine();
			if (username.endsWith("exit"))
				return;
			if (!accountservice.available(username))
				break;
			else
				System.out.println("Username already exists");

		} while (true);

		do {
			System.out.println("Enter password");
			pass1 = scan.nextLine();
			if (pass1.equals("exit"))
				return;
			if (pass1.length() < 6)
				System.out.println("Password must contain atleast 6 charecter");
			else
				break;
		} while (true);

		do {
			System.out.println("Re enter your password");
			pass2 = scan.nextLine();
			if (pass2.equals("exit"))
				return;
			if (pass2.equals(pass1))
				break;
			else
				System.out.println("Passwords do not match");
		} while (true);

		do {
			System.out.println("Enter your email address");
			email = scan.nextLine();
			if (email.equals("exit"))
				return;
			if (email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))
				break;
			else
				System.out.println("Enter a valid email address. Ex - abcd@xyz.com");
		} while (true);

		if (accountservice.register(username, pass1, email))
			menu(username);
	}

	private void login(String username) {

		String disp = accountservice.lastLogged(username);
		if (accountservice.updateLastLogged(username)) {
			System.out.println(disp);
			menu(username);
		}

	}

	private void menu(String username) {

		do {

			System.out.println("1. Balance");
			System.out.println("2. Deposit");
			System.out.println("3. Withdraw");
			System.out.println("4. Transfer");
			System.out.println("5. Set Pin");
			System.out.println("6. Transaction History");
			System.out.println("7. Techinical Support");
			System.out.println("8. Change Password");
			System.out.println("9. Schedule Payment");
			System.out.println("10. Loan Process");
			System.out.println("0. Back");
			String ch = scan.nextLine();
			switch (ch) {

			case "0":
				return;
			case "1":
				Double bal = accountservice.balance(username);
				if (bal == null) {
					System.out.println("Something went wrong");
				} else {
					System.out.println("Your balance is" + bal);
				}
				break;

			case "2":
				do {
					System.out.println("Type 'exit' to exit");
					System.out.println("Enter the amount to deposit");
					String amt = scan.nextLine();
					if (amt.equals("exit"))
						break;
					int rs = accountservice.deposit(username, amt);
					if (rs == -1) {
						System.out.println("Enter a valid number");
					} else if (rs == -2) {
						System.out.println("Error Occured. Try again!");
					} else if (rs == 0) {
						System.out.println("Deposited Successfully");
						tservice.add(username, "deposit", amt);
						break;
					}

				} while (true);
				break;

			case "3":
				do {
					System.out.println("Type 'exit' to exit");
					System.out.println("Enter the Amount to wtihdraw");
					String amt2 = scan.nextLine();
					if (amt2.equals("exit"))
						break;
					int rs = accountservice.withdraw(username, amt2);
					if (rs == -2) {
						break;
					}
					if (rs < 0)
						System.out.println("Enter a valid number");
					else if (rs == 0)
						System.out.println("Insuffiecient Balance");
					else {
						System.out.println("Withdraw of " + amt2 + "Successfully");
						tservice.add(username, "Withdraw", amt2);
						break;
					}

				} while (true);
				break;
			case "4":
				if (!transfer(username))
					return;
				break;
			case "5":
				setPin(username);
				break;
			case "6":
				transactionHistory(username);
				break;
			case "7":
				new SupportController().connect();
				break;
			case "8":
				changePass(username);
				break;
			case "9":
				schedule(username);
				break;
			case "10":
				loan(username);
			default:
				System.out.println("Enter a valid Option");
			}

		} while (true);

	}

	private void setPin(String username) {

		if (!accountservice.sendOtp(username)) {

			System.out.println("Error. Try again!!");
			return;
		}

		do {

			System.out.println("Enter the OTP sent to your mail");
			String otp = scan.nextLine();

			if (otp.equals("exit"))
				return;

			if (accountservice.checkOtp(Integer.parseInt(otp)))
				break;

			System.out.println("Incorrect OTP");

		} while (true);

		do {

			System.out.println("Enter 4-digit number");
			String pin = scan.nextLine();

			if (pin.equals("exit"))
				return;

			if (accountservice.setPin(username, pin))
				break;

			System.out.println("Enter a valid Number");

		} while (true);

	}

	private boolean transfer(String sender) {
		String reciever, amt, pin;
		System.out.println("Type 'exit' to exit");

		do {

			System.out.println("Enter the reciepents username");
			reciever = scan.nextLine();
			if (reciever.equals("exit"))
				return true;
			if (accountservice.available(reciever))
				break;
			System.out.println("No such user exists");

		} while (true);

		do {

			System.out.println("Enter the amount");
			amt = scan.nextLine();
			if (amt.equals("exit"))
				return true;
			if (!amt.matches("^[0-9]\\d{0,9}(\\.\\d{1,2})?%?$") || amt.length() > 10) {
				System.out.println("Enter a valid number");
				return true;
			} else
				break;

		} while (true);

		do {
			System.out.println("Enter pin");
			pin = scan.nextLine();
			if (pin.equals("exit"))
				return true;

			int res = accountservice.checkPin(sender, pin);

			if (res == -2) {
				System.out.println("Something Went wrong. Try again!!");
				return true;
			}
			if (res == 0) {
				System.out.println("Your account has been blocked");
				return false;
			}

			if (res < 0)
				break;
			System.out.println("Incorrect pin. You have " + (3 - res) + " chances left.");

		} while (true);

		String res = accountservice.transfer(sender, reciever, amt, pin);
		if (res.equals("success")) {
			tservice.add(sender, "Transfer to " + reciever, amt);
			tservice.add(reciever, "Recieved from " + sender, amt);
			System.out.println("Transaction Successfull");
			return true;
		}
		System.out.println(res);
		return true;
	}

	private void transactionHistory(String username) {

		System.out.println("Enter date in yyyy-mm-dd");
		System.out.println("Enter the start date");
		String start = scan.nextLine();
		System.out.println("Enter the end date");
		String end = scan.nextLine();
		String email = accountservice.getEmail(username);

		tservice.read(username, start, end, email);

	}

	private boolean changePass(String username) {

		do {

			System.out.println("Enter current password");
			String pass = scan.nextLine();
			if (pass.equals("exit"))
				return false;
			if (accountservice.login(username, pass))
				break;
//			System.out.println("Password Incorrect");

		} while (true);

		do {
			System.out.println("Enter new password");
			String pass1 = scan.nextLine();
			if (pass1.equals("exit"))
				return false;
			System.out.println("Re Enter new password");
			String pass2 = scan.nextLine();
			if (pass2.equals("exit"))
				return false;
			if (pass1.length() < 6 || pass2.length() < 6) {
				System.out.println("Password should contain minimum 6 charecters");
				continue;
			}
			if (pass1.equals(pass2)) {
				int rs = accountservice.changePass(username, pass1);
				if (rs == -1) {
					System.out.println("Something went wrong try again");
					return false;
				}
				if (rs == 1) {
					System.out.println("Password changed");
					return true;
				}
				System.out.println("you cannot enter last 3 passwords");

			} else
				System.out.println("Passwords do not match");

		} while (true);

	}

	private void schedule(String from) {
		String to, amt, year, month, date, hour, min;
		do {
			System.out.println("Enter reciepents name");
			to = scan.nextLine();
			if (to.equals("exit"))
				return;

			if (accountservice.available(to))
				break;
			System.out.println("No such user exists");

		} while (true);

		do {
			System.out.println("Enter the amount");
			amt = scan.nextLine();
			if (amt.equals("exit"))
				return;
			if (!amt.matches("^[0-9]\\d{0,9}(\\.\\d{1,2})?%?$") || amt.length() > 10)
				System.out.println("Enter a vlid number");
			else
				break;
		} while (true);

		do {
			System.out.println("Enter year");
			year = scan.nextLine();
			if (year.equals("exit"))
				return;
			if (year.matches("^[0-9]{4}$"))
				break;
			System.out.println("Enter a valid year");
		} while (true);

		do {
			System.out.println("Enter month");
			month = scan.nextLine();
			if (month.equals("exit"))
				return;
			if (month.matches("^[0-9]{2}$"))
				break;
			System.out.println("Enter a valid month");
		} while (true);

		do {

			System.out.println("Enter date");
			date = scan.nextLine();
			if (date.equals("exit"))
				return;
			if (date.matches("^[0][1-9]|[1][0-2]$"))
				break;
			System.out.println("Enter a valid date");

		} while (true);

		do {

			System.out.println("Enter hour");
			hour = scan.nextLine();
			if (hour.equals("exit"))
				return;
			if (hour.matches("^(0[0-9]|1[0-9]|2[0-3])$"))
				break;
			System.out.println("Enter a valid hour");

		} while (true);

		do {
			System.out.println("Enter minute");
			min = scan.nextLine();

			if (min.equals("exit"))
				return;
			if (min.matches("^[0-5][0-9]$"))
				break;
			System.out.println("Enter a valid minute");

		} while (true);

		accountservice.schedule(from, to, Double.parseDouble(amt), year, month, date, hour, min);
	}

	private void loan(String username) {

		do {

			System.out.println("1. Request Loan");
			System.out.println("2. Cancel Request");
			System.out.println("3. Repay");
			System.out.println("4. Exit");
			String ch = scan.nextLine();
			switch (ch) {

			case "1":
				loanRequest(username);
				break;
			case "2":
				rservice.cancel(username);
				break;
			case "3":
				rservice.repay(username);
				break;
			case "4":
				return;
			default:
				System.out.println("Enter a valid option");
				break;

			}

		} while (true);
	}
	
	private void loanRequest(String username) {
		String description, amt;
		do {
			System.out.println("Enter Description");
			description = scan.nextLine();
			if(description.equals("exit"))
				return;
			if(description.length() != 0)
				break;
			System.out.println("Enter a description");
		}while(true);
		
		do {
			System.out.println("Enter Loan amount");
			amt = scan.nextLine();
			if(amt.equals("exit"))
				return;
			if (!amt.matches("^[0-9]\\d{0,9}(\\.\\d{1,2})?%?$") || amt.length() > 10) {
				System.out.println("Enter a valid number");
				continue;
			}
			break;
		}while(true);
		
		rservice.request(username, description, amt);
	}
	

}
