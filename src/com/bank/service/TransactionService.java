package com.bank.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.bank.domain.Transaction;
import com.bank.repository.TransactionRepository;


public class TransactionService {

	TransactionRepository transactiondata = new TransactionRepository();

	public void add(String username, String to, String amt) {

		Transaction t = new Transaction();
		t.username = username;
		t.description = to;
		t.amount = Double.parseDouble(amt);
		t.date = Date.valueOf(LocalDate.now());
		transactiondata.add(t);
	}

	public void read(String username, String startStr, String endStr, String email) {

		if (!startStr.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")
				|| !endStr.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")) {
			System.out.println("Enter the date in the valid format");
			return;
		}

		Date start = Date.valueOf(LocalDate.parse(startStr));
		Date end = Date.valueOf(LocalDate.parse(endStr));
		List<Transaction> list = transactiondata.read(username, start, end);
		
		if(list == null) {
			System.out.println("Error occured. Try again!");
			return;
		}

		if(list.size() == 0) {
			System.out.println("No transaction in that period");
			return;
		}
		try {

			BufferedWriter br = new BufferedWriter(new FileWriter("transaction.txt"));
			int i = 1;
			for (Transaction t : list) {
				br.write(i + ". " + t.description + " " + t.amount + " " + t.date);
				br.newLine();
				i++;
			}
			br.close();
			System.out.println("Transaction sent to your E-mail");
			new Thread(()-> SendMail.sendAttach(email, "Transaction History", new File("transaction.txt"))).start();
		} catch (IOException e) {
			System.out.println("Error Occured");
//			e.printStackTrace();
		}

	}

}
