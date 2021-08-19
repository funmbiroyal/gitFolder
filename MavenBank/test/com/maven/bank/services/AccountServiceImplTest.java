package com.maven.bank.services;

import com.maven.bank.datastore.*;
import com.maven.bank.entitities.*;
import com.maven.bank.exception.MavenBankException;
import com.maven.bank.exception.MavenBankInsufficientFundsException;
import com.maven.bank.exception.MavenBankTransactionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {
    private AccountService accountService;
    private Customer abu;
    private Customer bessie;
    private SavingsAccount abuSavingsAccount;
    private Account bessieAccount;
    private SavingsAccount johnCurrentAccount;


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
        assertTrue(abu.getAccounts().isEmpty());
        assertEquals(1000110003, BankService.getCurrentAccountNumber());

        try {

            long newAccountNumber = accountService.openSavingsAccount(abu);

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
        assertThrows(MavenBankException.class, () -> accountService.openAccount(null, AccountType.SAVINGS_ACCOUNT));
    }

    @Test
    void openAccountForCurrentAccount(){
        assertTrue(abu.getAccounts().isEmpty());
       assertEquals(1000110003,BankService.getCurrentAccountNumber());
    try{
        long newAccountNumber = accountService.openCurrentAccount(abu);
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
        System.out.println(john.getAccounts().get(0).getClass().getTypeName());
        System.out.println(john.getAccounts().get(0).getClass().getSimpleName());
       // assertEquals(AccountType.SAVINGS, john.getAccounts().get(0).getTypeOfAccount());
        System.out.println(AccountType.SAVINGS_ACCOUNT.toString());
        assertEquals(1000110003, BankService.getCurrentAccountNumber());
        assertEquals(2,john.getAccounts().size());

    }


    @Test
    void openDifferentTypeOfAccountForSameCustomer() {
        try {
            long newAccountNumber = accountService.openSavingsAccount(abu);

            assertFalse(CustomerRepo.getCustomers().isEmpty());
            assertEquals(1000110004, BankService.getCurrentAccountNumber());
            assertTrue(CustomerRepo.getCustomers().containsKey(abu.getBvn()));
            assertFalse(abu.getAccounts().isEmpty());
            assertEquals(newAccountNumber, abu.getAccounts().get(0).getAccountNumber());
            newAccountNumber = accountService.openCurrentAccount(abu);
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
            long newAccountNumber = accountService.openSavingsAccount(abu);
            assertFalse(CustomerRepo.getCustomers().isEmpty());
            assertEquals(1000110004, BankService.getCurrentAccountNumber());
            assertTrue(CustomerRepo.getCustomers().containsKey(abu.getBvn()));
            assertFalse(abu.getAccounts().isEmpty());
            assertEquals(newAccountNumber, abu.getAccounts().get(0).getAccountNumber());

            newAccountNumber = accountService.openSavingsAccount(bessie);
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
            Account johnSavingAccount = accountService.findAccount(1000110001);
            assertNotNull(johnSavingAccount);
            BigDecimal accountBalance = accountService.deposit(new BigDecimal(500000),1000110001);
            johnSavingAccount = accountService.findAccount(1000110001);
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
            Account johnCurrentAccount = accountService.findAccount(1000110002);
            assertNotNull(johnCurrentAccount);
            assertEquals(1000110002,johnCurrentAccount.getAccountNumber());

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
            Account johnSavingAccount = accountService.findAccount(1000110001);
            assertNotNull(johnSavingAccount);
            BigDecimal depositAmount = new BigDecimal("1000000000000000000");
            BigDecimal accountBalance = accountService.deposit(depositAmount,1000110001);
            johnSavingAccount = accountService.findAccount(1000110001);
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
            Account johnSavingAccount = accountService.findAccount(1000110001);
            BigDecimal accountBalance = accountService.deposit(new BigDecimal(500000),1000110001);
            johnSavingAccount = accountService.findAccount(1000110001);
            assertEquals(accountBalance,johnSavingAccount.getBalance());

            accountBalance = accountService.withdraw(new BigDecimal(100000),1000110001);
            johnSavingAccount = accountService.findAccount(1000110001);
            assertEquals(accountBalance, johnSavingAccount.getBalance());
        } catch (MavenBankException e) {
            e.printStackTrace();
        }
    }
    @Test
    void withdrawNegativeAmount(){
        assertThrows(MavenBankTransactionException.class,()->accountService.withdraw
                (new BigDecimal(-10000),1000110001));
    }
    @Test
    void withdrawMoreThanTheBalance(){
        try {
            Account johnSavingAccount = accountService.findAccount(1000110001);
            assertNotNull(johnSavingAccount);
            BigDecimal accountBalance = accountService.withdraw(new BigDecimal(500000000),1000110001);
            johnSavingAccount = accountService.findAccount(1000110001);
            assertEquals(accountBalance,johnSavingAccount.getBalance());

        } catch (MavenBankException e) {
            e.printStackTrace();
        }
        assertThrows(MavenBankInsufficientFundsException.class,()->accountService.withdraw(new BigDecimal(600000),1000110001));

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
    @Test
    void applyForLoan(){
        LoanRequest johnLoanRequest = new LoanRequest();
        johnLoanRequest.setApplyDate(LocalDateTime.now());
        johnLoanRequest.setLoanAmount(BigDecimal.valueOf(50000000));
        johnLoanRequest.setInterestRate(0.1);
        johnLoanRequest.setStatus(LoanRequestStatus.NEW);
        johnLoanRequest.setTenor(24);
        johnLoanRequest.setTypeOfLoan(LoanType.SME);

        try{
           Account johnCurrentAccount = accountService.findAccount(1000110002) ;
           assertNotNull(johnCurrentAccount);
        johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);
           assertNotNull(johnCurrentAccount);

        }catch (MavenBankException e){
            e.printStackTrace();
        }

    }
    @Test
    void addBankTransactionWithNullTransaction(){
        assertThrows(MavenBankTransactionException.class,()-> accountService.addBankTransaction(null,abuSavingsAccount));
    }
    @Test
    void addBankTransactionWithNullAccount(){
        BankTransaction transaction = new BankTransaction(BankTransactionType.DEPOSIT_TRANSACTION,BigDecimal.valueOf(50));
        assertThrows(MavenBankTransactionException.class,()-> accountService.addBankTransaction(transaction,null));
    }

    @Test
    void addBankTransactionWithDeposit(){
        try {
            Account janeSavingsAccount = accountService.findAccount(1000110003);
            assertNotNull(janeSavingsAccount);
            assertEquals(BigDecimal.ZERO,janeSavingsAccount.getBalance());
            assertEquals(0,janeSavingsAccount.getTransactions().size());
            BankTransaction janeDeposit = new BankTransaction(BankTransactionType.DEPOSIT_TRANSACTION,BigDecimal.valueOf(10000));
            accountService.addBankTransaction(janeDeposit,janeSavingsAccount);
            assertEquals(BigDecimal.valueOf(10000),janeSavingsAccount.getBalance());
            assertEquals(1,janeSavingsAccount.getTransactions().size());
        }catch (MavenBankException ex){
            ex.printStackTrace();
        }
    }

    @Test
    void addBankTransactionWithNegativeDeposit(){
        try {
            Account janeSavingsAccount = accountService.findAccount(1000110003);
            assertNotNull(janeSavingsAccount);
            assertEquals(BigDecimal.ZERO,janeSavingsAccount.getBalance());
            assertEquals(0,janeSavingsAccount.getTransactions().size());

            BankTransaction janeDeposit = new BankTransaction(BankTransactionType.DEPOSIT_TRANSACTION,BigDecimal.valueOf(-10000));
            assertThrows(MavenBankTransactionException.class,()->accountService.addBankTransaction(janeDeposit,janeSavingsAccount));
            assertEquals(BigDecimal.ZERO,janeSavingsAccount.getBalance());
            assertEquals(0,janeSavingsAccount.getTransactions().size());


            assertEquals(0,janeSavingsAccount.getTransactions().size());
        }catch (MavenBankException ex){
            ex.printStackTrace();
        }

    }

    @Test
    void addBankTransactionForWithdrawal(){
        try{
            Account janeSavingsAccount = accountService.findAccount(1000110003);
            BankTransaction depositTx = new BankTransaction(BankTransactionType.DEPOSIT_TRANSACTION,BigDecimal.valueOf(50000));
            assertNotNull(janeSavingsAccount);
            accountService.addBankTransaction(depositTx,janeSavingsAccount);
            assertEquals(BigDecimal.valueOf(50000),janeSavingsAccount.getBalance());
            assertEquals(1,janeSavingsAccount.getTransactions().size());

            BankTransaction withdrawalTx = new BankTransaction(BankTransactionType.WITHDRAWAL_TRANSACTION,BigDecimal.valueOf(20000));
            accountService.addBankTransaction(withdrawalTx,janeSavingsAccount);
            assertEquals(BigDecimal.valueOf(30000),janeSavingsAccount.getBalance());
            assertEquals(2,janeSavingsAccount.getTransactions().size());
        }catch (MavenBankException ex){
            ex.printStackTrace();
        }


    }
    @Test
    void addBankTransactionWithNegativeWithdrawal(){
        try{
            Account janeSavingsAccount = accountService.findAccount(1000110003);
            assertNotNull(janeSavingsAccount);

            BankTransaction depositTx = new BankTransaction(BankTransactionType.DEPOSIT_TRANSACTION,BigDecimal.valueOf(50000));
            assertNotNull(janeSavingsAccount);
            accountService.addBankTransaction(depositTx,janeSavingsAccount);
            assertEquals(BigDecimal.valueOf(50000),janeSavingsAccount.getBalance());
            assertEquals(1,janeSavingsAccount.getTransactions().size());

            BankTransaction withdrawalTx = new BankTransaction(BankTransactionType.WITHDRAWAL_TRANSACTION,BigDecimal.valueOf(-20000));
            assertThrows(MavenBankTransactionException.class,()->accountService.addBankTransaction(withdrawalTx,janeSavingsAccount));
            assertEquals(BigDecimal.valueOf(50000),janeSavingsAccount.getBalance());
            assertEquals(1,janeSavingsAccount.getTransactions().size());

        }catch (MavenBankException ex){
            ex.printStackTrace();
        }
    }
}