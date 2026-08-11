package com.iyed.energypulse;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CustomerRepository{

    private final Map<String,Customer> customers = new HashMap<>();

    public void save(Customer customer){
        customers.put(customer.getCustomerId(), customer);
    }

    public Customer findById(String customerId){
        return customers.get(customerId);
    }
}