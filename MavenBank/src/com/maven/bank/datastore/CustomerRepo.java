package com.maven.bank.datastore;

import com.maven.bank.Customer;

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


}
