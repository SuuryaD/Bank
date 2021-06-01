
import java.util.Scanner;

import com.bank.controller.AccountController;
import com.bank.controller.AdminController;
import com.bank.controller.SupportController;



public class DriverClass {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		do {
			System.out.println("1. User");
			System.out.println("2. Admin");
			System.out.println("3. Support");
			System.out.println("0. Exit");
			
			String ch = scan.nextLine();
			
			switch(ch) {
			case "0":
				scan.close();
				System.exit(0);
				break;
			case "1":
				new AccountController().drive();
				break;
			case "2":
				System.out.println("Admin mode");
				new AdminController().login();
				break;
			case "3":
				System.out.println("Support Mode");
				new SupportController().login();
				break;
			default: 
				System.out.println("Enter a valid option");
				break;
			}
		}while(true);
	}

}
