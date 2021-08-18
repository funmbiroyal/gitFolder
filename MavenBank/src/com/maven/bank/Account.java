package com.maven.bank;

import com.maven.bank.datastore.AccountType;

import java.math.BigDecimal;

public class Account {
    private long accountName;
    private AccountType typeOfAccount;
    private BigDecimal balance = BigDecimal.ZERO;
    private String accountPin = "1224";
    private long accountNumber;

    public Account() {

    }
    public Account(long accountNumber, AccountType type) {
        this.accountNumber = accountNumber;
        this.typeOfAccount = type;
    }
    public Account(int accountNumber, AccountType type, BigDecimal amount) {
        this(accountNumber, type);
        this.balance = amount;

    }




    public long getAccountNumber() {
        return accountNumber;
    }

    public long getAccountName() {
        return accountName;
    }

    public void setAccountName(long accountName) {
        this.accountName = accountName;
    }

    public AccountType getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(AccountType typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountPin() {
        return accountPin;
    }

    public void setAccountPin(String accountPin) {
        this.accountPin = accountPin;
    }


    public void setAccountNumber(long s) {
    }
}
