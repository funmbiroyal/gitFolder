package com.maven.bank.datastore;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.services.BankService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepo {
    private static Map<Long, Customer> customers = new HashMap<>();

    public static Map<Long, Customer> getCustomers() {
        return customers;
    }

    public static void setCustomers(Map<Long, Customer> customers) {
        CustomerRepo.customers = customers;
    }

    static {
             setUp();
    }


    public static void setUp() {
        Customer john = new Customer();
        john.setBvn(1);
        john.setFirstName("Grace");
        john.setSurname("Ajadi");
        john.setAddress("Sabo,Yaba,Lagos");
        john.setEmail("funmibiajadi@gmail.com");
        john.setPhoneNumber("0907654321");

        Account johnAccount = new Account(0000110001,AccountType.SAVINGS);

        john.getAccounts().add(johnAccount);

        Account johnCurrentAccount = new Account(0000110002,AccountType.CURRENT,new BigDecimal(50000000));


        john.getAccounts().add(johnCurrentAccount);
        customers.put(john.getBvn(),john);

        Customer jane = new Customer();
        jane.setBvn(2);
        jane.setFirstName("Grace");
        jane.setSurname("Ajadi");
        jane.setAddress("Sabo,Yaba,Lagos");
        jane.setEmail("funmibiajadi@gmail.com");
        jane.setPhoneNumber("0907654321");

        Account janeCurrentAccount = new Account(0000110003,AccountType.CURRENT,new BigDecimal(50000000));
        jane.getAccounts().add(janeCurrentAccount);
        customers.put(jane.getBvn(),jane);


    }
}
