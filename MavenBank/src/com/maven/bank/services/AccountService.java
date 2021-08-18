package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.TransactionType;
import com.maven.bank.exception.MavenBankException;

import java.math.BigDecimal;

public interface AccountService {

  long openAccount(Customer theCustomer, AccountType type) throws MavenBankException;
  BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException;
  Account findAccount (long accountNumber) throws MavenBankException;
  Account findAccount (Customer customer,long accountNumber);


  BigDecimal withdraw(BigDecimal amount, long accountNumber) throws MavenBankException;
  void ApplyForOverDraft(Account account);
}
