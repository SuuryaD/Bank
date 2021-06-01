package com.bank.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bank.domain.Request;
import com.bank.repository.AccountRepository;
import com.bank.repository.LoanRepository;
import com.bank.repository.RequestRepository;

public class AdminService {

	AccountRepository accountdata = new AccountRepository();
	RequestRepository requestdata = new RequestRepository();
	LoanRepository loandata = new LoanRepository();
	Scanner scan = new Scanner(System.in);
	
	public boolean login(String username, String pass) {
		if(username.equals("admin") && pass.equals("admin"))
			return true;
		return false;
		
	}

	public void unblock() {

		ArrayList<String> list = accountdata.getBlocked();
		
		int i = 0;
		boolean outer = false;
		do {
			
			if(outer)
				return;
			
			if (list.size() == 0) {
				System.out.println("No Blocked users");
				return;
			}
			
			if(list.size() == 1) {
				
				boolean flag = false;

				do {
					if (flag)
						break;
					System.out.println("Username: " + list.get(0));
					System.out.println("1. unblock");
					System.out.println("0. Exit");
					String ch = scan.nextLine();
					switch (ch) {
					
					case "0":
						flag = true;
						outer = true;
						break;
					case "1":
						accountdata.block(list.get(0), false);
						list = accountdata.getBlocked();
						flag = true;
						break;
					default:
						System.out.println("Enter a valid option");
						break;
					}
				} while (true);
			}

			else if (i > 0 && i < list.size() - 1) {
				boolean flag = false;

				do {
					if (flag)
						break;
					System.out.println("Username: " + list.get(i));
					System.out.println("1. unblock");
					System.out.println("2. Next");
					System.out.println("3. Previous");
					System.out.println("0. Exit");
					String ch = scan.nextLine();
					switch (ch) {
					
					case "0":
						flag = true;
						outer = true;
						break;
						
					case "1":
						accountdata.block(list.get(i), false);
						list = accountdata.getBlocked();
						flag = true;
						break;
						
					case "2":
						i++;
						flag = true;
						break;
						
					case "3":
						i--;
						flag = true;
						break;
						
					default:
						System.out.println("Enter a valid option");
						break;
					}
				} while (true);

			} else if (i == 0) {
				boolean flag = false;

				do {
					if (flag)
						break;
					System.out.println("Username: " + list.get(i));
					System.out.println("1. unblock");
					System.out.println("2. Next");
					System.out.println("3. Exit");
					String ch = scan.nextLine();
					switch (ch) {
					case "1":
						accountdata.block(list.get(i), false);
						list = accountdata.getBlocked();
						flag = true;
						break;
					case "2":
						i++;
						flag = true;
						break;
					case "3":
						flag = true;
						outer = true;
						break;
					default:
						System.out.println("Enter a valid option");
						break;
					}
				} while (true);

			} else if (i == list.size() - 1) {
				boolean flag = false;

				do {
					if (flag)
						break;
					System.out.println("Username: " + list.get(i));
					System.out.println("1. unblock");
					System.out.println("2. Previous");
					System.out.println("3. Exit");
					String ch = scan.nextLine();
					switch (ch) {
					case "1":
						accountdata.block(list.get(i), false);
						list = accountdata.getBlocked();
						flag = true;
						break;
					case "2":
						i--;
						flag = true;
						break;
					case "3":
						flag = true;
						outer = true;
						break;
					default:
						System.out.println("Enter a valid option");
						break;
					}
				} while (true);
			}

		} while (true);

	}
	
	public void unblock2(String username) {
		
		if(!accountdata.available(username)) {
			System.out.println("No such user exists");
			return;
		}
		
		if(accountdata.getBlock(username) == 0) {
			System.out.println("The user account is not blocked");
			return;
		}
		
		if(accountdata.block(username, false) == 1) {
			new Thread(() -> SendMail.send(accountdata.getEmail(username), "Unblocked", "Your account has been unblocked. You can access it now")).start();
			System.out.println("Account has been unblocked");
		}
		else
			System.out.println("Something went wrong. Try again");
		
	}

	public void block(String username) {
		if(!accountdata.available(username)) {
			System.out.println("No such user exists");
			return;
		}
		
		if(accountdata.getBlock(username) == 1) {
			System.out.println("The user account is already blocked");
			return;
		}
		if(accountdata.block(username, true) != 1) {
			System.out.println("Something went wrong. Try again!");
			return;
		}
	}
	
	public void process() {
		List<Request> list = requestdata.getAll();
		if(list == null) {
			System.out.println("Something went wrong. Try again");
			return;
		}
		
		boolean outer = false;
		int i = 0;
		do {
			if (list.size() == 0) {
				System.out.println("No Loan request");
				return;
			}
			if (outer)
				break;
			if (list.size() == 1) {
				boolean inner = false;

				do {
					if (inner)
						break;
					System.out.println("Loan No: " + list.get(0).loan_no);
					System.out.println("Description :" + list.get(0).description);
					System.out.println("Amount :" + list.get(0).amount);
					System.out.println("Requested Date :" + list.get(0).requested_date);
					System.out.println("1. Accept");
					System.out.println("2. Reject");
					System.out.println("0. Exit");
					String ch = scan.nextLine();

					switch (ch) {
					case "0":
						inner = true;
						outer = true;
						break;

					case "1":
						accept(list.get(0));
						list = requestdata.getAll();
						
						inner = true;
						break;
					case "2":
						reject(list.get(0).loan_no, list.get(0).username);
						list = requestdata.getAll();
						inner = true;
						break;
					default:
						System.out.println("Enter a valid option");
						break;
					}

				} while (true);
			}
			else if (i == list.size() - 1) {
				boolean inner = false;

				do {
					if (inner)
						break;
					System.out.println("Loan No: " + list.get(i).loan_no);
					System.out.println("Description :" + list.get(i).description);
					System.out.println("Amount :" + list.get(i).amount);
					System.out.println("Requested Date :" + list.get(i).requested_date);
					System.out.println("1. Accept");
					System.out.println("2. Reject");
					System.out.println("3. Previous");
					System.out.println("0. Exit");
					String ch = scan.nextLine();

					switch (ch) {
					case "0":
						inner= true;
						outer = true;
						break;
					case "1":
						accept(list.get(i));
						list = requestdata.getAll();
						i = list.size() -1 ;
						inner = true;
						break;
					case "2":
						reject(list.get(i).loan_no, list.get(i).username);
						list = requestdata.getAll();
						i = list.size() - 1;
						inner = true;
						break;
					case "3":
						i--;
						inner = true;
						break;
					default:
						System.out.println("Enter a valid option");
						break;
					}

				} while (true);
			}

			else if (i == 0) {
				boolean inner = false;

				do {
					if (inner)
						break;
					System.out.println("Loan No: " + list.get(i).loan_no);
					System.out.println("Description :" + list.get(i).description);
					System.out.println("Amount :" + list.get(i).amount);
					System.out.println("Requested Date :" + list.get(i).requested_date);
					System.out.println("1. accept");
					System.out.println("2. Reject");
					System.out.println("3. Next");
					System.out.println("0. Exit");
					String ch = scan.nextLine();

					switch (ch) {
					
					case "0":
						inner = true;
						outer = true;
						break;
						
					case "1":
						accept(list.get(i));
						list = requestdata.getAll();
						inner = true;
						break;
					case "2":
						reject(list.get(i).loan_no, list.get(i).username);
						list = requestdata.getAll();
						inner = true;
						break;
					case "3":
						i++;
						inner = true;
						break;
					default:
						System.out.println("Enter a valid option");
						break;
					}

				} while (true);

			}

			else {
				boolean inner = false;

				do {
					if (inner)
						break;
					System.out.println("Loan No: " + list.get(i).loan_no);
					System.out.println("Description :" + list.get(i).description);
					System.out.println("Amount :" + list.get(i).amount);
					System.out.println("Requested Date :" + list.get(i).requested_date);
					System.out.println("1. Accept");
					System.out.println("2. Reject");
					System.out.println("3. Next");
					System.out.println("4. Previous");
					System.out.println("0. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "0":
						inner = true;
						outer = true;
						break;
					case "1":
						accept(list.get(i));
						list = requestdata.getAll();
						inner = true;
						break;
					case "2":
						reject(list.get(i).loan_no, list.get(i).username);
						list = requestdata.getAll();
						inner = true;
						break;
					case "3":
						i++;
						inner = true;
						break;
					case "4":
						i--;
						inner = true;
						break;
					default:
						System.out.println("Enter a valid option");
						break;
					}

				} while (true);
			}

		} while (true);

		
		
	}
	
	private void accept(Request r) {
		
		Double bal = accountdata.getBalance(r.username);
		if(bal == null) {
			System.out.println("Something went wrong. Try again!");
			return;
		}
		
		String date;
		do {
			System.out.println("Enter the due date. (yyyy-mm-dd) format");
			date = scan.nextLine();
			if(date.equals("exit"))
				return;
			if(date.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$"))
				break;
			System.out.println("Enter date in yyyy-mm-dd format");
		}while(true);
		
		if(accountdata.updateBalance(r.username, bal + r.amount) == 0){
			System.out.println("Something went wrong. Try again");
			return;
		}
		loandata.insert(r, Date.valueOf(LocalDate.parse(date)));
		requestdata.cancel(r.loan_no);
		new Thread(() ->SendMail.send(accountdata.getEmail(r.username), "Loan Process", "Your loan has been accepted. The amount "+r.amount + " has been deposited")).start();
		
		System.out.println("Loan Accepted");
		
	}
	
	private void reject(int loan_no, String username) {
		
		if(requestdata.cancel(loan_no) == 0) {
			System.out.println("Something went wrong. Try again!");
			return;
		}
		
		new Thread(() ->SendMail.send(accountdata.getEmail(username), "Loan Process", "Your Loan ("+loan_no+" has been rejected")).start();
	}
}
