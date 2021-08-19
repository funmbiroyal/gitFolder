package com.maven.bank.services;

import com.maven.bank.datastore.LoanRequestStatus;
import com.maven.bank.entitities.*;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.datastore.BankTransactionType;
import com.maven.bank.exception.MavenBankException;
import com.maven.bank.exception.MavenBankInsufficientFundsException;
import com.maven.bank.exception.MavenBankTransactionException;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService{

    @Override
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException {
        long accountNumber =BigDecimal.ZERO.longValue();
        if (type == AccountType.SAVINGS_ACCOUNT){
            openSavingsAccount(theCustomer);
        }else if (type == AccountType.CURRENT_ACCOUNT){
            openCurrentAccount(theCustomer);
        }
        return accountNumber;
    }

    @Override
    public long openSavingsAccount(Customer theCustomer) throws MavenBankException {
        if(theCustomer == null){
            throw new MavenBankException("No Customer and account type required to open Account");
        }
        SavingsAccount newAccount = new SavingsAccount();
        if (accountTypeExist(theCustomer,newAccount.getClass().getTypeName())){
            throw new MavenBankException("Account already exist");

        }

        newAccount.setAccountNumber(BankService.generateAccountNumber());
        CustomerRepo.getCustomers().put(theCustomer.getBvn(),theCustomer);
        theCustomer.getAccounts().add(newAccount);
        return newAccount.getAccountNumber();
    }

    @Override
    public long openCurrentAccount(Customer theCustomer) throws MavenBankException {
        if(theCustomer == null){
            throw new MavenBankException("No Customer and account type required to open Account");
        }
        CurrentAccount newAccount = new CurrentAccount();
        if (accountTypeExist(theCustomer,newAccount.getClass().getTypeName())){
            throw new MavenBankException("Account already exist");

        }

        newAccount.setAccountNumber(BankService.generateAccountNumber());
        CustomerRepo.getCustomers().put(theCustomer.getBvn(),theCustomer);
        theCustomer.getAccounts().add(newAccount);
        return newAccount.getAccountNumber();

    }

    @Override
    public void addBankTransaction(BankTransaction transaction, Account account) throws MavenBankException {
     if (transaction == null || account == null){
         throw new MavenBankTransactionException("Transaction and account are required to add transaction!");
     }if(transaction.getType()== BankTransactionType.DEPOSIT_TRANSACTION){
         deposit(transaction.getAmount(),account.getAccountNumber());

        }else if (transaction.getType()== BankTransactionType.WITHDRAWAL_TRANSACTION){
         withdraw(transaction.getAmount(),account.getAccountNumber());

        }
        account.getTransactions().add(transaction);


    }

    @Override
    public BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException {
        BigDecimal newBalance = BigDecimal.ZERO;
        Account depositAccount = findAccount(accountNumber);
        validateBankingTransactions(amount,depositAccount, BankTransactionType.DEPOSIT_TRANSACTION);
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
        return foundAccount ;
    }

    @Override
    public BigDecimal withdraw(BigDecimal amountToWithdraw, long accountNumber) throws MavenBankException {

        Account theAccount = findAccount(accountNumber);
        validateBankingTransactions(amountToWithdraw,theAccount, BankTransactionType.WITHDRAWAL_TRANSACTION);
        try{
            checkSufficientBalance(amountToWithdraw,theAccount);
        }catch (MavenBankInsufficientFundsException insufficientFundsException){
            this.ApplyForOverDraft(theAccount);
            throw insufficientFundsException;

        }

        BigDecimal newBalance = debitAccount(amountToWithdraw,accountNumber);
       return newBalance;
    }

    private boolean accountTypeExist(Customer aCustomer, String typeName){
        boolean accountTypeExist = false;
        for (Account customerAccount : aCustomer.getAccounts()){

            if (customerAccount.getClass().getTypeName() =="typeName"){
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

    private void validateBankingTransactions(BigDecimal amount, Account account, BankTransactionType type) throws MavenBankInsufficientFundsException {

            if (amount.compareTo(BigDecimal.ZERO) < BigDecimal.ZERO.intValue()) {
                throw new MavenBankInsufficientFundsException("Transaction amount can not be negative!");
            }
            if (account == null){
                throw new MavenBankInsufficientFundsException("Deposit Account not exist!");
            }
           if (type == BankTransactionType.WITHDRAWAL_TRANSACTION) {
               //get back later!
           throw new MavenBankInsufficientFundsException("Insufficient funds");
           }


    }
    private void checkSufficientBalance(BigDecimal amount,Account account)throws MavenBankTransactionException{
        if (amount.compareTo(account.getBalance()) > BigDecimal.ZERO.intValue()){
            throw new MavenBankInsufficientFundsException("Insufficient funds");
        }
    }

    @Override
    public void ApplyForOverDraft(Account account) {
      //TODO
    }

    @Override
    public LoanRequestStatus ApplyForLoan(Account account) {
        return null;

    }
}
