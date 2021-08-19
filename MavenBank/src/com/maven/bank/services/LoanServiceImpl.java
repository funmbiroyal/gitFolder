package com.maven.bank.services;

import com.maven.bank.datastore.LoanRequestStatus;
import com.maven.bank.entitities.Account;
import com.maven.bank.entitities.Customer;
import com.maven.bank.entitities.LoanRequest;
import com.maven.bank.exception.MavenBankLoanException;

import java.math.BigDecimal;

public class LoanServiceImpl implements LoanService {
    @Override
    public LoanRequest approveLoanRequest(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        LoanRequestStatus decision = decideOnLoanRequestWithLengthOfRelationshipAndTransactionVolume(customer,accountSeekingLoan);
        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest();
        theLoanRequest.setStatus(decision);

        if (decision!= LoanRequestStatus.APPROVED){
            theLoanRequest = approveLoanRequest(accountSeekingLoan);
        }
        return theLoanRequest;
    }
    @Override
    public LoanRequest approveLoanRequest(Account accountSeekingLoan) throws MavenBankLoanException {
        if (accountSeekingLoan == null){
            throw new MavenBankLoanException("An account is required!");
        }
        if (accountSeekingLoan.getAccountLoanRequest()==null){
            throw new MavenBankLoanException("No loan request provided for processing");
        }
        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest();
        theLoanRequest.setStatus(decideOnLoanRequest(accountSeekingLoan));
        return theLoanRequest;
    }

    private LoanRequestStatus decideOnLoanRequest(Account accountSeekingLoan) throws MavenBankLoanException {

        LoanRequestStatus decision = decideOnLoanRequestWithAccountBalance(accountSeekingLoan);
       return decision;

    }
    private LoanRequestStatus decideOnLoanRequestWithAccountBalance(Account accountSeekingLoan){
        LoanRequestStatus decision = LoanRequestStatus.PENDING;
        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest();
        BigDecimal accountBalancePercentage = BigDecimal.valueOf(0.2);
        BigDecimal loanAmountApprovedAutomatically = accountSeekingLoan.getBalance().multiply(accountBalancePercentage);
        if (theLoanRequest.getLoanAmount().compareTo(loanAmountApprovedAutomatically) < BigDecimal.ZERO.intValue()){
            decision = LoanRequestStatus.APPROVED;
        }
        return decision;
    }

    private LoanRequestStatus decideOnLoanRequestWithLengthOfRelationshipAndTransactionVolume(Customer customer,Account accountSeekingLoan){
        LoanRequestStatus decision = LoanRequestStatus.PENDING;
        int minimumLengthOfRelationship = 6;
        BigDecimal relationshipVolumePercentage =  BigDecimal.valueOf(0.2);
        BigDecimal totalCustomerBalance = BigDecimal.ZERO;
        if (customer.getAccounts().size() > BigDecimal.ONE.intValue()){
            for (Account customerAccount : customer.getAccounts()){
                totalCustomerBalance = totalCustomerBalance.add(customerAccount.getBalance());

            }
        }

      BigDecimal loanAmountApprovedAutomatically = totalCustomerBalance.multiply(relationshipVolumePercentage);
        if (accountSeekingLoan.getAccountLoanRequest().getLoanAmount().compareTo(loanAmountApprovedAutomatically)< BigDecimal.ZERO.intValue())
            decision = LoanRequestStatus.APPROVED;
        return decision;
    }
}
