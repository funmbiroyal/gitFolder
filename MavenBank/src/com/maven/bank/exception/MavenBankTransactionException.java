package com.maven.bank.exception;

public class MavenBankTransactionException extends MavenBankException {
    public MavenBankTransactionException(String message) {
        super(message);

    }

    public  MavenBankTransactionException(String message,Throwable ex){

    }
    public MavenBankTransactionException(Throwable ex){

    }
}
