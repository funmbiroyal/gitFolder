package com.maven.bank.services;

import java.time.Year;

public class BankService {
    private static long currentBVN = 2;
    private static long currentAccountNumber = 1000110003;
    private static Year openingYear = Year.now().minusYears(2);
    private static long currentTransactionId = 0;


    public static Year getOpeningYear() {
        return openingYear;
    }


    public static void setOpeningYear(Year openingYear) {
        BankService.openingYear = openingYear;
    }
    public static long getCurrentBVN() {
        return currentBVN;
    }

    private static void setCurrentBVN(long currentBVN) {
        BankService.currentBVN = currentBVN;
    }

    public static long getCurrentAccountNumber() {
        return currentAccountNumber;
    }

    private static void setCurrentAccountNumber(long currentAccountNumber) {
        BankService.currentAccountNumber = currentAccountNumber;
    }
    private static void setCurrentTransactionId(long currentTransactionId) {
        BankService.currentTransactionId= currentTransactionId;
    }

    public static long getCurrentTransactionId() {
        return currentTransactionId;
    }

    public static long generateBVN(){
        currentBVN++;
        return  currentBVN;
    }

    public static long generateTransactionId(){
        currentTransactionId++;
        return  currentTransactionId;
    }

    public static long generateAccountNumber(){
       ++currentAccountNumber;
       return  currentAccountNumber;
    }

    public static void reset() {
        currentBVN = 2;
        currentAccountNumber = 1000110003;
    }
}
