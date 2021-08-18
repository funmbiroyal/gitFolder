package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exception.MavenBankException;
import com.maven.bank.exception.MavenBankInsufficientFundsException;
import com.maven.bank.exception.MavenBankTransactionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {
    private AccountService accountService;
    private Customer abu;
    private Customer bessie;
    private Account abuAccount;
    private Account bessieAccount;


    @BeforeEach
    void setUp() {
        abu = new Customer();
        accountService = new AccountServiceImpl();
        abu.setBvn(BankService.generateBVN());
        abu.setEmail("john@dee.com");
        abu.setFirstName("john");
        abu.setSurname("dee");
        abu.setPhoneNumber("09087654321");

        bessie = new Customer();
        accountService = new AccountServiceImpl();
        bessie.setBvn(BankService.generateBVN());
        bessie.setEmail("jane@bush.com");
        bessie.setFirstName("jane");
        bessie.setSurname("bush");
        abu.setPhoneNumber("07034254321");
        bessie.setPassword("1224");
    }

    @AfterEach
    void tearDown() {
        BankService.reset();
        CustomerRepo.setUp();

    }

    @Test
    void openSavingsAccount() {
        try {
            assertTrue(abu.getAccounts().isEmpty());
            //assertTrue(CustomerRepo.getCustomers().isEmpty());
            assertEquals(1000110003, BankService.getCurrentAccountNumber());
            assertFalse(CustomerRepo.getCustomers().containsKey(abu.getBvn()));

            long newAccountNumber = accountService.openAccount(abu, AccountType.SAVINGS);

            assertFalse(CustomerRepo.getCustomers().isEmpty());
            assertEquals(1000110004, BankService.getCurrentAccountNumber());
            assertTrue(CustomerRepo.getCustomers().containsKey(abu.getBvn()));
            assertFalse(abu.getAccounts().isEmpty());
            assertEquals(newAccountNumber, abu.getAccounts().get(0).getAccountNumber());

        } catch (MavenBankException e) {
            e.printStackTrace();
        }
    }

    @Test
    void openAccountWithNoCustomer() {
        assertThrows(MavenBankException.class, () -> accountService.openAccount(null, AccountType.SAVINGS));
    }

    @Test
    void openAccountWithNoAccountType() {
        assertThrows(MavenBankException.class, () -> accountService.openAccount(abu, null));
    }

    @Test
    void openAccountForCurrentAccount(){
        assertTrue(abu.getAccounts().isEmpty());
       assertEquals(1000110003,BankService.getCurrentAccountNumber());
    try{
        long newAccountNumber = accountService.openAccount(abu,AccountType.CURRENT);
        assertEquals(1000110004,BankService.getCurrentAccountNumber());
        assertTrue(CustomerRepo.getCustomers().containsKey(abu.getBvn()));
        assertFalse(abu.getAccounts().isEmpty());
        assertEquals(newAccountNumber,abu.getAccounts().get(0).getAccountNumber());

    }catch (MavenBankException ex){
           ex.printStackTrace();
    }
    }

    @Test
    void cannotOpenSameTypeOfAccountForSameCustomer() {


        Optional<Customer> johnOptional = CustomerRepo.getCustomers().values().stream().findFirst();
        Customer john = (johnOptional.isEmpty()) ? null :johnOptional.get();
        assertEquals(1000110003, BankService.getCurrentAccountNumber());
        assertNotNull(john);
        assertNotNull(john.getAccounts());
        assertFalse(john.getAccounts().isEmpty());
        assertEquals(AccountType.SAVINGS, john.getAccounts().get(0).getTypeOfAccount());

        assertThrows(MavenBankException.class, () -> accountService.openAccount(john, AccountType.SAVINGS));
        assertEquals(1000110003, BankService.getCurrentAccountNumber());
        assertEquals(2,john.getAccounts().size());

    }


    @Test
    void openDifferentTypeOfAccountForSameCustomer() {
        try {
            long newAccountNumber = accountService.openAccount(abu, AccountType.SAVINGS);

            assertFalse(CustomerRepo.getCustomers().isEmpty());
            assertEquals(1000110004, BankService.getCurrentAccountNumber());
            assertTrue(CustomerRepo.getCustomers().containsKey(abu.getBvn()));
            assertFalse(abu.getAccounts().isEmpty());
            assertEquals(newAccountNumber, abu.getAccounts().get(0).getAccountNumber());
            newAccountNumber = accountService.openAccount(abu, AccountType.CURRENT);
            assertEquals(1000110005, BankService.getCurrentAccountNumber());
            assertEquals(2, abu.getAccounts().size());
            assertEquals(newAccountNumber, abu.getAccounts().get(1).getAccountNumber());

        } catch (MavenBankException e) {
            e.printStackTrace();
        }


    }

    @Test
    void openSavingsAccountForNewCustomer() {
        assertTrue(abu.getAccounts().isEmpty());
        assertEquals(1000110003,BankService.getCurrentAccountNumber());
        try {
            long newAccountNumber = accountService.openAccount(abu, AccountType.SAVINGS);
            assertFalse(CustomerRepo.getCustomers().isEmpty());
            assertEquals(1000110004, BankService.getCurrentAccountNumber());
            assertTrue(CustomerRepo.getCustomers().containsKey(abu.getBvn()));
            assertFalse(abu.getAccounts().isEmpty());
            assertEquals(newAccountNumber, abu.getAccounts().get(0).getAccountNumber());

            newAccountNumber = accountService.openAccount(bessie, AccountType.SAVINGS);
            assertEquals(1000110005, BankService.getCurrentAccountNumber());
            assertEquals(4, CustomerRepo.getCustomers().size());
            assertTrue(CustomerRepo.getCustomers().containsKey(bessie.getBvn()));
            assertFalse(bessie.getAccounts().isEmpty());
            assertEquals(1,bessie.getAccounts().size());
            assertEquals(newAccountNumber, bessie.getAccounts().get(0).getAccountNumber());


        } catch (MavenBankException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deposit() {
        try {
            Account johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(BigDecimal.ZERO,johnSavingAccount.getBalance());
            BigDecimal accountBalance = accountService.deposit(new BigDecimal(500000),0000110001);
            johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(accountBalance,johnSavingAccount.getBalance());
        } catch (MavenBankTransactionException ex) {
            ex.printStackTrace();
        } catch (MavenBankException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    void findAccount() {
        try {
            Account johnCurrentAccount = accountService.findAccount(0000110002);
            assertNotNull(johnCurrentAccount);
            assertEquals(0000110002,johnCurrentAccount.getAccountNumber());

        } catch (MavenBankException ex) {
            ex.printStackTrace();
        }
    }
    @Test
    void findAccountWithInvalidNumber(){
        try{
            Account johnCurrentAccount = accountService.findAccount(2000);
            assertNull(johnCurrentAccount);
        }catch (MavenBankException ex){
            ex.printStackTrace();
        }
    }
    @Test
    void depositWithVeryLargeAmount(){

        try {
            Account johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(BigDecimal.ZERO,johnSavingAccount.getBalance());
            BigDecimal depositAmount = new BigDecimal("1000000000000000000");
            BigDecimal accountBalance = accountService.deposit(depositAmount,0000110001);
            johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(accountBalance,johnSavingAccount.getBalance());
        } catch (MavenBankTransactionException ex) {
            ex.printStackTrace();
        } catch (MavenBankException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void depositNegativeAmount(){
        assertThrows(MavenBankTransactionException.class,()-> accountService.deposit(new BigDecimal(-500000),0000110001));
    }

    @Test
    void withdraw(){
        try {
            Account johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(BigDecimal.ZERO,johnSavingAccount.getBalance());
            BigDecimal accountBalance = accountService.deposit(new BigDecimal(500000),0000110001);
            johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(accountBalance,johnSavingAccount.getBalance());

            accountBalance = accountService.withdraw(new BigDecimal(100000),0000110001);
            johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(accountBalance, johnSavingAccount.getBalance());
        } catch (MavenBankException e) {
            e.printStackTrace();
        }
    }
    @Test
    void withdrawNegativeAmount(){
        assertThrows(MavenBankTransactionException.class,()->accountService.withdraw
                (new BigDecimal(-10000),0000110001));
    }
    @Test
    void withdrawMoreThanTheBalance(){ 
        try {
            Account johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(BigDecimal.ZERO,johnSavingAccount.getBalance());
            BigDecimal accountBalance = accountService.deposit(new BigDecimal(500000),0000110001);
            johnSavingAccount = accountService.findAccount(0000110001);
            assertEquals(accountBalance,johnSavingAccount.getBalance());

        } catch (MavenBankException e) {
            e.printStackTrace();
        }
        assertThrows(MavenBankInsufficientFundsException.class,()->accountService.withdraw(new BigDecimal(600000),0000110001));

    }
    @Test
    void depositWithInvalidAccountNumber(){
        assertThrows(MavenBankTransactionException.class,()-> accountService.deposit(new BigDecimal(500000),10001));
    }
    @Test
    void withdrawWithInvalidAccountNumber(){
        assertThrows(MavenBankTransactionException.class,()->accountService.withdraw
                (new BigDecimal(10000),10001));
    }
}