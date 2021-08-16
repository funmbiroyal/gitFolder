package com.maven.bank.services;

import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.exception.MavenBankException;

public interface AccountService {

   long openAccount(Customer theCustomer, AccountType type) throws MavenBankException;
}
