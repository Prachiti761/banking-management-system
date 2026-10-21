package com.bankapp.model;

public class CurrentAccount extends Account {

	public CurrentAccount(Long accountNumber, int customerId, double balance, String status) {
		super(accountNumber, customerId, balance, status);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAccountType() {
		// TODO Auto-generated method stub
		return "CURRENT";
	}

	@Override
	public double calculateInterest() {
		// TODO Auto-generated method stub
		return 0;
	}

}
