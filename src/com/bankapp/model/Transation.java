package com.bankapp.model;

import java.sql.Timestamp;

public class Transation {

    private int transationId = 0;

    private final long accountNumber;

    private final String type;

    private final double amount;

    private final String description;

    private final Timestamp date;

    public Transation(int transationId, long accountNumber, String type,
                      double amount, String description, Timestamp date) {

        super();

        this.transationId = transationId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public int getTransactionId() {
        return transationId;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDesription() {
        return description;
    }

    public Timestamp getDate() {
        return date;
    }
}