package com.bank.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.bank.domain.Loan;
import com.bank.domain.Request;
import com.bank.repository.AccountRepository;
import com.bank.repository.LoanRepository;
import com.bank.repository.RequestRepository;

public class RequestService {

	RequestRepository requestdata = new RequestRepository();
	AccountRepository accountdata = new AccountRepository();
	LoanRepository loandata = new LoanRepository();
	Scanner scan = new Scanner(System.in);

	public void request(String username, String description, String amt) {
		int loan_no;
		do {
			Random r = new Random(System.currentTimeMillis());
			loan_no = 10000 + r.nextInt(20000);
			int rs = requestdata.checkLoanNo(loan_no);
			if (rs == 1)
				continue;
			else if (rs == -2) {
				System.out.println("Something went wrong");
				return;
			}
			break;

		} while (true);

		Request r = new Request();
		r.loan_no = loan_no;
		r.amount = Double.parseDouble(amt);
		r.username = username;
		r.description = description;
		r.requested_date = Date.valueOf(LocalDate.now());
		requestdata.insert(r);

		System.out.println("Loan request Successfull. Your loan No: " + loan_no);
	}

	public void cancel(String username) {

		List<Request> list = requestdata.getAll(username);
		if (list == null) {
			System.out.println("Something went wrong");
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
					System.out.println("Loan No: " + list.get(i).loan_no);
					System.out.println("Description :" + list.get(i).description);
					System.out.println("Amount :" + list.get(i).amount);
					System.out.println("Requested Date :" + list.get(i).requested_date);
					System.out.println("1. Cancel");
					System.out.println("2. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "1":
						if (requestdata.cancel(list.get(i).loan_no) == 0) {
							System.out.println("Somethig went wrong");
						}
						list = requestdata.getAll(username);
						inner = true;
						break;
					case "2":
						inner = true;
						outer = true;
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
					System.out.println("1. Cancel");
					System.out.println("2. Previous");
					System.out.println("3. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "1":
						if (requestdata.cancel(list.get(i).loan_no) == 0) {
							System.out.println("Somethig went wrong");
						}
						list = requestdata.getAll(username);
						inner = true;
						break;
					case "2":
						i--;
						inner = true;
						break;
					case "3":
						inner = true;
						outer = true;
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
					System.out.println("1. Cancel");
					System.out.println("2. Next");
					System.out.println("3. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "1":
						if (requestdata.cancel(list.get(i).loan_no) == 0) {
							System.out.println("Somethig went wrong");
						}
						list = requestdata.getAll(username);
						inner = true;
						break;
					case "2":
						i++;
						inner = true;
						break;
					case "3":
						inner = true;
						outer = true;
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
					System.out.println("1. Cancel");
					System.out.println("2. Next");
					System.out.println("3. Previous");
					System.out.println("4. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "1":
						if (requestdata.cancel(list.get(i).loan_no) == 0) {
							System.out.println("Somethig went wrong");
						}
						list = requestdata.getAll(username);
						inner = true;
						break;
					case "2":
						i++;
						inner = true;
						break;
					case "3":
						i--;
						inner = true;
						break;
					case "4":
						inner = true;
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
	
	
	public void repay(String username) {
		
		List<Loan> list = loandata.getAll(username);
		
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
					System.out.println("Sanctioned Date :" + list.get(0).sdate);
					System.out.println("Due date : " + list.get(0).due_date);
					System.out.println("1. Repay");
					System.out.println("2. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "1":
						pay(list.get(0));
						list = loandata.getAll(username);
						inner = true;
						break;
					case "2":
						inner = true;
						outer = true;
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
					System.out.println("Requested Date :" + list.get(i).sdate);
					System.out.println("Due Date: " + list.get(i).due_date);
					System.out.println("1. Repay");
					System.out.println("2. Previous");
					System.out.println("3. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "1":
						pay(list.get(i));
						list = loandata.getAll(username);
						i = list.size() - 1;
						inner = true;
						break;
					case "2":
						i--;
						inner = true;
						break;
					case "3":
						inner = true;
						outer = true;
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
					System.out.println("Requested Date :" + list.get(i).sdate);
					System.out.println("Due Date: " + list.get(i).due_date);
					System.out.println("1. Repay");
					System.out.println("2. Next");
					System.out.println("3. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "1":
						pay(list.get(i));
						list = loandata.getAll(username);
						inner = true;
						break;
					case "2":
						i++;
						inner = true;
						break;
					case "3":
						inner = true;
						outer = true;
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
					System.out.println("Requested Date :" + list.get(i).sdate);
					System.out.println("Due Date: " + list.get(i).due_date);
					System.out.println("1. Cancel");
					System.out.println("2. Next");
					System.out.println("3. Previous");
					System.out.println("4. Exit");
					String ch = scan.nextLine();

					switch (ch) {

					case "1":
						pay(list.get(i));
						list = loandata.getAll(username);
						inner = true;
						break;
					case "2":
						i++;
						inner = true;
						break;
					case "3":
						i--;
						inner = true;
						break;
					case "4":
						inner = true;
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
	
	private void pay(Loan l) {
		
		Double bal = accountdata.getBalance(l.username);
		
		if(bal < l.amount) {
			System.out.println("Insufficient Balance");
			return;
		}
		
		if(accountdata.updateBalance(l.username, bal - l.amount) != 1) {
			System.out.println("Something went wrong. Try again!");
			return;
		}
		
		loandata.cancel(l.loan_no);
		new TransactionService().add(l.username, "Loan Repayment", l.amount.toString());
		System.out.println("Loan has been repayed successfully");
	}
	
}
