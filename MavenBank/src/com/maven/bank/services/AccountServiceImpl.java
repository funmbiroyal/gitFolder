package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exception.MavenBankException;

public class AccountServiceImpl implements AccountService{
    @Override
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException {
        if(theCustomer == null || type == null){
            throw new MavenBankException("No Customer and account type required to open Account");
        }
        if (accountTypeExist(theCustomer,type)){
            throw new MavenBankException("Account already exist");

        }
        Account newAccount = new Account();
        newAccount.setAccountNumber(BankService.generateAccountNumber());
        newAccount.setTypeOfAccount(type);
        CustomerRepo.getCustomers().put(theCustomer.getBvn(),theCustomer);
        theCustomer.getAccounts().add(newAccount);
        return newAccount.getAccountNumber();

    }
    private boolean accountTypeExist(Customer aCustomer, AccountType type){
        boolean accountTypeExist = false;
        for (Account customerAccount : aCustomer.getAccounts()){
            if (customerAccount.getTypeOfAccount()==type){
                accountTypeExist = true;
                break;
            }
        }
        return accountTypeExist;
    }
}
