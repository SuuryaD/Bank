package com.bank.domain;

import java.io.Serializable;
import java.sql.Date;

public class Loan implements Serializable {

	
	public int loan_no;
	public String username;
	public Double amount;
	public String description;
	public Date sdate;
	public Date due_date;
}
