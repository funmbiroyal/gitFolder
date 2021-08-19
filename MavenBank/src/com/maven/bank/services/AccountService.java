package com.maven.bank.services;

import com.maven.bank.datastore.LoanRequestStatus;
import com.maven.bank.entitities.Account;
import com.maven.bank.entitities.BankTransaction;
import com.maven.bank.entitities.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.exception.MavenBankException;
import com.maven.bank.exception.MavenBankTransactionException;

import java.math.BigDecimal;

public interface AccountService {

  long openAccount(Customer theCustomer, AccountType type) throws MavenBankException;
  long openSavingsAccount(Customer theCustomer) throws MavenBankException;
  long openCurrentAccount(Customer theCustomer) throws MavenBankException;
  BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException;
  Account findAccount (long accountNumber) throws MavenBankException;
  //Account findAccount (Customer customer,long accountNumber);
  BigDecimal withdraw(BigDecimal amount, long accountNumber) throws MavenBankException;
  void ApplyForOverDraft(Account account);
  LoanRequestStatus ApplyForLoan(Account account);
  public void addBankTransaction(BankTransaction transaction,Account account) throws MavenBankException;

}
