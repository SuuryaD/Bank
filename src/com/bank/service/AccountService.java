package com.bank.service;


import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.bank.domain.Account;
import com.bank.repository.AccountRepository;

public class AccountService {

	AccountRepository accountdata;
	private static int otp;

	public AccountService() {
		accountdata = new AccountRepository();
	}

	public boolean login(String username, String pass) {

		int res = accountdata.getBlock(username);
		
		if(res == -1) {
			System.out.println("Internal Error. Try again");
			return false;
		}
		if (res == 1) {
			System.out.println("Your account has been blocked");
			return false;
			
		}

		Account acc = accountdata.getUser(username);
		if (acc == null) {
			System.out.println("Username or password incorrect");
			return false;
		}
		if (acc.username.equals(username) && acc.pass.equals(hash(pass)))
			return true;
		else {
			System.out.println("Username or password incorrect");
			return false;
		}

	}

	public boolean available(String username) {
		Account acc = accountdata.getUser(username);
		if (acc == null)
			return false;
		return true;
	}

	public boolean register(String username, String pass, String email) {

		Account acc = new Account();
		acc.username = username;
		acc.pass = hash(pass);
		acc.pin = null;
		acc.email = email;
		acc.last_logged = (Timestamp) new Timestamp(new Date().getTime());
		acc.last_changed = (Timestamp) new Timestamp(new Date().getTime());
		acc.amount = 0;

		if (accountdata.insert(acc) != 1)
			return false;

		return true;

	}

	public String lastLogged(String username) {

		Timestamp ts = accountdata.getLast(username);
		if(ts == null) {
			return "error occuered";
		}
		Date date = new Date();
		date.setTime(ts.getTime());
		String formatted = new SimpleDateFormat("dd/MM/yyyy hh.mm aa").format(date);
		return "You last logged in on " + formatted;
	}

	public boolean updateLastLogged(String username) {

		if(accountdata.updateLastLogged(username, (Timestamp) new Timestamp(new Date().getTime())) == 1)
			return true;
		return false;

	}

	public Double balance(String username) {
		return accountdata.getBalance(username);

	}

	public int deposit(String username, String amt) {

		if (!amt.matches("^[0-9]\\d{0,9}(\\.\\d{1,2})?%?$") || amt.length() > 10)
			return -1;
		else {
			Double bal = balance(username);
			if(bal == null)
				return -2;
			if(accountdata.updateBalance(username, Double.parseDouble(amt) + bal) == 1)
				return 0;
			else
				return -2;
		}
	}

	public int withdraw(String username, String amt) {

		if (!amt.matches("^[0-9]\\d{0,9}(\\.\\d{1,2})?%?$") || amt.length() > 10)
			return -1;

		Double bal = balance(username);
		
		if(bal == null) {
			System.out.println("Something went wrong. Try again!");
			return -2;
		}
		
		if (Double.parseDouble(amt) > bal) {
			return 0;
		}
		if(accountdata.updateBalance(username, bal - Double.parseDouble(amt)) == 1)
			return 1;
		else {
			System.out.println("Something went wrong. Try again!");
			return -2;
		}
			
	}

	public String transfer(String sender, String reciever, String amt, String pin) {
		
		if (!amt.matches("^[0-9]\\d{0,9}(\\.\\d{1,2})?%?$") || amt.length() > 10)
			return "Enter a valid number";


		if (!accountdata.getPin(sender).equals(pin))
			return "Incorrect Pin";

		
		Double bal = accountdata.getBalance(sender);
		if(bal == null)
			return "Error occured. Try again";
		
		if(bal < Double.parseDouble(amt))
			return "Insufficient Balance";
		
		if(accountdata.transfer(sender, reciever, Double.parseDouble(amt)))
			return "success";
		
		return "Transaction Failed";
	}

	public boolean setPin(String username, String pin) {

		if (!pin.matches("^[0-9]{4}$")) {
			return false;
		}

		if(accountdata.addPin(username, pin))
			return true;
		return false;
	}

	public int checkPin(String username, String pin) {

		String pin2 = accountdata.getPin(username);
		if(pin2.equals(""))
			return -2;
		int count = accountdata.getPinCount(username);
		if(count == -1) {
			return -2;
		}
		
		if (pin.equals(pin2)) {
			if(accountdata.updatePinCount(username, 0) == 1)
				return -1;
			return -2;
		}

		if (count + 1 == 3) {
			if(accountdata.block(username, true) == 1)
				return 0;
			return -2;
		}

		if(accountdata.updatePinCount(username, count + 1) == 1)
			return count + 1;
		return -2;

	}

	public String getEmail(String username) {
		return accountdata.getEmail(username);
	}

	public boolean sendOtp(String username) {

		String email = getEmail(username);
		if(email.equals(""))
			return false;
		Random r = new Random(System.currentTimeMillis());
		otp = 10000 + r.nextInt(20000);
		new Thread(() ->SendMail.send(email, "OTP to set pin", Integer.toString(otp))).start();

		return true;

	}

	public boolean checkOtp(int otpb) {
		
		if (otp == otpb) {
			otp = -1;
			return true;
		}
		return false;

	}
	
	public int changePass(String username, String pass1) {

		List<String> list = accountdata.getPass(username);
		if(list.size() == 0) {
			return -1;
		}
		pass1 = hash(pass1);
		if (pass1.equals(list.get(0)) || pass1.equals(list.get(1)) || pass1.equals(list.get(2))
				|| pass1.equals(list.get(3)))
			return 0;

		if(accountdata.updatePass(username, pass1, list.get(0), list.get(1), list.get(2),
				(Timestamp) new Timestamp(new Date().getTime())) == 1)
			return 1;
		return -1;

	}

	public int lastChanged(String username) {

		Timestamp ts = accountdata.getLastChanged(username);
		if(ts == null)
			return -1;
		Timestamp now = (Timestamp) new Timestamp(new Date().getTime());
		long milliseconds = now.getTime() - ts.getTime();
		int seconds = (int) milliseconds/1000;
		int minutes = (seconds % 3600) / 60;
		System.out.println(minutes);
		System.out.println(milliseconds);
		if(milliseconds >= 600000)
			return 1;
		return 0;


	}

	public void schedule(String from, String to, double amt, String year, String month, String date, String hour, String min) {

		
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(7);
		for (int i = 0; i < 7; i++) {
			  
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());
  
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                          .charAt(index));
        }
  
		List<String> commands = new ArrayList<String>();
		commands.add("schtasks.exe");
		commands.add("/CREATE");
		commands.add("/TN");
//		commands.add("\"HowToTask\"");
		commands.add("\""+sb.toString()+"\"");
		commands.add("/TR");
		commands.add("\"E:\\Dhanush\\sch.bat\"");
		commands.add("/SC");
		commands.add("once");
		commands.add("/ST");
		commands.add(hour+":"+min+":00");
		commands.add("/SD");
		commands.add(month+"/"+date+"/"+year);
//		commands.add("/z");

		
		ProcessBuilder builder = new ProcessBuilder(commands);
		  Process p;
		try {
			p = builder.start();
//			p.waitFor();
//			System.out.println(p.exitValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(accountdata.insertshedule(from, to, amt, Timestamp.valueOf(year+"-"+month+"-"+date + " " + hour+":"+min+":00")) != 1) {
			System.out.println("Error. Try Again");
			return;
		}
		  
	}

	public static String hash(String input) {

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] messageDigest = md.digest(input.getBytes());

			BigInteger no = new BigInteger(1, messageDigest);

			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
