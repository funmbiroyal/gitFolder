package com.maven.bank.engines;

import com.maven.bank.datastore.LoanRequestStatus;
import com.maven.bank.entitities.Account;
import com.maven.bank.entitities.Customer;
import com.maven.bank.exception.MavenBankLoanException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class LoanEngineByRelationshipLength implements LoanEngine {
    @Override
    public BigDecimal calculateAmountAutoApproved(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        BigDecimal approveLoan = BigDecimal.ZERO;
        validateLoanRequest(customer, accountSeekingLoan);
        BigDecimal totalCustomerBalance = BigDecimal.ZERO;
        if (customer.getAccounts().size() > BigDecimal.ONE.intValue()) {
            for (Account customerAccount : customer.getAccounts()) {
                totalCustomerBalance = totalCustomerBalance.add(customerAccount.getBalance());
//                System.out.println(totalCustomerBalance);

                long period = ChronoUnit.MONTHS.between(
                        customer.getRelationshipStartDate(),LocalDateTime.now());

                if (period >= 24) {
                    approveLoan = totalCustomerBalance.multiply(BigDecimal.valueOf(0.10));
                } else if (period >= 18) {
                    approveLoan = totalCustomerBalance.multiply(BigDecimal.valueOf(0.08));
                } else if (period >= 12 ) {
                    approveLoan = totalCustomerBalance.multiply(BigDecimal.valueOf(0.06));
                } else if (period >= 6) {
                    approveLoan = totalCustomerBalance.multiply(BigDecimal.valueOf(0.04));
                } else if (period >= 3) {
                    approveLoan = totalCustomerBalance.multiply(BigDecimal.valueOf(0.02));
                } else if (period >= 0) {
                    approveLoan = totalCustomerBalance.multiply(BigDecimal.valueOf(0.00));
                }
                else{
                    throw new MavenBankLoanException("Invalid popo!");
//                    System.out.println();
                }
            }

            //calculate amount auto approved based on total balance and length Of relationship

        }
        return approveLoan;
    }
}

