package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exception.MavenBankException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {
    private AccountService accountService;
    Customer john;
    Customer jane;
    Account johnAccount;


    @BeforeEach
    void setUp() {
        john = new Customer();
        accountService = new AccountServiceImpl();
        john.setBvn(BankService.generateBVN());
        john.setEmail("john@dee.com");
        john.setFirstName("john");
        john.setSurname("dee");
        john.setPhoneNumber("09087654321");

        jane = new Customer();
        accountService = new AccountServiceImpl();
        jane.setBvn(BankService.generateBVN());
        jane.setEmail("jane@bush.com");
        jane.setFirstName("jane");
        jane.setSurname("bush");
        john.setPhoneNumber("07034254321");
    }

   @Test
    void openSavingsAccount(){
       try {
           assertTrue(john.getAccounts().isEmpty());
           assertTrue(CustomerRepo.getCustomers().isEmpty());
           assertEquals(0,BankService.getCurrentAccountNumber());

           long newAccountNumber = accountService.openAccount(john, AccountType.SAVINGS);

           assertFalse(CustomerRepo.getCustomers().isEmpty());
           assertEquals(1,BankService.getCurrentAccountNumber());
           assertTrue(CustomerRepo.getCustomers().containsKey(john.getBvn()));
           assertFalse(john.getAccounts().isEmpty());
           assertEquals(newAccountNumber,john.getAccounts().get(0).getAccountNumber());

       } catch (MavenBankException e) {
           e.printStackTrace();
       }
    }

   @Test
    void openAccountWithNoCustomer(){
        assertThrows(MavenBankException.class,()-> accountService.openAccount(null, AccountType.SAVINGS));
   }
   @Test
    void cannotOpenSameTypeOfAccountForSameCustomer(){

       try {
           long newAccountNumber = accountService.openAccount(john, AccountType.SAVINGS);

           assertFalse(CustomerRepo.getCustomers().isEmpty());
           assertEquals(1,BankService.getCurrentAccountNumber());
           assertTrue(CustomerRepo.getCustomers().containsKey(john.getBvn()));
           assertFalse(john.getAccounts().isEmpty());
           assertEquals(newAccountNumber,john.getAccounts().get(0).getAccountNumber());
           assertThrows(MavenBankException.class,()->accountService.openAccount(john,AccountType.SAVINGS));

       } catch (MavenBankException e) {
           e.printStackTrace();
       }

   }
   @Test
    void  openDifferentTypeOfAccountForSameCustomer(){
       try {
           long newAccountNumber = accountService.openAccount(john, AccountType.SAVINGS);

           assertFalse(CustomerRepo.getCustomers().isEmpty());
           assertEquals(1,BankService.getCurrentAccountNumber());
           assertTrue(CustomerRepo.getCustomers().containsKey(john.getBvn()));
           assertFalse(john.getAccounts().isEmpty());
           assertEquals(newAccountNumber,john.getAccounts().get(0).getAccountNumber());
           //assertThrows(MavenBankException.class,()->accountService.openAccount(john,AccountType.CURRENT));
            newAccountNumber = accountService.openAccount(john,AccountType.CURRENT);
            assertEquals(2,BankService.getCurrentAccountNumber());
            assertEquals(2,john.getAccounts().size());
            assertEquals(newAccountNumber,john.getAccounts().get(1).getAccountNumber());

       } catch (MavenBankException e) {
           e.printStackTrace();
       }


   }
    @Test
    void openSavingsAccountForNewCustomer(){
            try {
                long newAccountNumber = accountService.openAccount(john,AccountType.SAVINGS);
                assertFalse(CustomerRepo.getCustomers().isEmpty());
                assertEquals(1,BankService.getCurrentAccountNumber());
                assertTrue(CustomerRepo.getCustomers().containsKey(john.getBvn()));
                assertFalse(john.getAccounts().isEmpty());
                assertEquals(newAccountNumber,john.getAccounts().get(0).getAccountNumber());

                newAccountNumber = accountService.openAccount(jane, AccountType.SAVINGS);
                assertFalse(CustomerRepo.getCustomers().isEmpty());
                assertEquals(2,BankService.getCurrentAccountNumber());
                assertEquals(2,CustomerRepo.getCustomers().size());
                assertEquals(newAccountNumber,jane.getAccounts().get(0).getAccountNumber());


            } catch (MavenBankException e) {
                e.printStackTrace();
            }
    }

}