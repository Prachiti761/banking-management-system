package com.bankapp.model;

public class SavingAccount extends Account {

	public static final double INTEREST_RATE=0.4;
	public SavingAccount(Long accountNumber, int customerId, double balance, String status) {
		super(accountNumber, customerId, balance, status);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAccountType() {
		// TODO Auto-generated method stub
		return "SAVINGS";
	}

	@Override
	public double calculateInterest() {
		// TODO Auto-generated method stub
		return getBalance() * INTEREST_RATE;
	}

}
