package com.maven.bank;

import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.services.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    Customer john;
    Account johnAccount;

    @BeforeEach
    void setUp(){
        john = new Customer ();
        johnAccount = new Account();
    }
    @Test
    void openAccount(){
     john.setBvn(BankService.generateBVN());
     john.setFirstName("Grace");
     john.setSurname("Ajadi");
     john.setAddress("Sabo,Yaba,Lagos");
     john.setEmail("funmibiajadi@gmail.com");
     johnAccount.setTypeOfAccount(AccountType.SAVINGS);
     johnAccount.setAccountPin("1224");
     johnAccount.setAccountNumber(BankService.generateAccountNumber());
     john.getAccounts().add(johnAccount);

     assertTrue(CustomerRepo.getCustomers().isEmpty());

    }
}
