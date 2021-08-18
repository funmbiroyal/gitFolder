package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.datastore.TransactionType;
import com.maven.bank.exception.MavenBankException;
import com.maven.bank.exception.MavenBankInsufficientFundsException;
import com.maven.bank.exception.MavenBankTransactionException;

import java.math.BigDecimal;

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

    @Override
    public BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException {
        BigDecimal newBalance = BigDecimal.ZERO;
        Account depositAccount = findAccount(accountNumber);
        validateBankingTransactions(amount,depositAccount,TransactionType.DEPOSIT_TRANSACTION);
        newBalance = depositAccount.getBalance().add(amount);
        depositAccount.setBalance(newBalance);

        return newBalance;
    }

    @Override
    public Account findAccount(long accountNumber) throws MavenBankException {
        Account foundAccount = null;
        boolean accountFound = false;

            for (Customer customer: CustomerRepo.getCustomers().values()){
                for (Account anAccount : customer.getAccounts()){
                    if (anAccount.getAccountNumber() == accountNumber){
                        foundAccount = anAccount;
                        accountFound = true;
                        break;
                    }
                }
                if(accountFound){
                    break;
                }
            }
        return foundAccount;
    }

    @Override
    public Account findAccount(Customer customer, long accountNumber) {
        return null;
    }

    @Override
    public BigDecimal withdraw(BigDecimal amountToWithdraw, long accountNumber) throws MavenBankException {

        Account theAccount = findAccount(accountNumber);
        validateBankingTransactions(amountToWithdraw,theAccount,TransactionType.WITHDRAWAL_TRANSACTION);
        try{
            checkSufficientBalance(amountToWithdraw,theAccount);
        }catch (MavenBankInsufficientFundsException insufficientFundsException){
            this.ApplyForOverDraft(theAccount);

        }

        BigDecimal newBalance = debitAccount(amountToWithdraw,accountNumber);
       return newBalance;
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
    public BigDecimal debitAccount(BigDecimal amount,long accountNumber) throws MavenBankException {
        Account foundAccount = findAccount(accountNumber);
        BigDecimal newBalance = foundAccount.getBalance().subtract(amount);
        foundAccount.setBalance(newBalance);
        return newBalance;

    }

//    private void validateWithdrawalTransaction(BigDecimal amountToWithdraw,long accountNumber) throws MavenBankException {
//        Account theAccount = findAccount(accountNumber);
//        if (theAccount == null){
//            throw new MavenBankTransactionException("Deposit Account Not found!");
//        }
//        if (amountToWithdraw.compareTo(theAccount.getBalance()) > 0){
//            throw  new MavenBankInsufficientFundsException("Insufficient Funds");
//        }
//        if (amountToWithdraw.compareTo(BigDecimal.ZERO) < 0){
//            throw new MavenBankTransactionException("Deposit amount should be positive!");
//        }
//    }

//    private void validateDepositTransaction(BigDecimal amount, Account account) throws MavenBankTransactionException {
//        if (amount.compareTo(BigDecimal.ZERO) < 0){
//            throw new MavenBankTransactionException("Deposit amount should be positive!");
//        }
//        if (account == null){
//            throw new MavenBankTransactionException("Deposit Account not exist!");
//        }
//    }

    private void validateBankingTransactions(BigDecimal amount, Account account,TransactionType type) throws MavenBankInsufficientFundsException {

            if (amount.compareTo(BigDecimal.ZERO) < BigDecimal.ZERO.intValue()) {
                throw new MavenBankInsufficientFundsException("Deposit amount should be positive!");
            }
            if (account == null){
                throw new MavenBankInsufficientFundsException("Deposit Account not exist!");
            }
           if (type == TransactionType.WITHDRAWAL_TRANSACTION) {
           throw new MavenBankInsufficientFundsException("Insufficient funds");
           }


    }
    private void checkSufficientBalance(BigDecimal amount,Account account)throws MavenBankTransactionException{
        if (amount.compareTo(account.getBalance()) > 0){
            throw new MavenBankInsufficientFundsException("Insufficient funds");
        }
    }

    @Override
    public void ApplyForOverDraft(Account account) {

    }

}
