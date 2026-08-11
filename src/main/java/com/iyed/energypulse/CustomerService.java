package com.iyed.energypulse;

import org.springframework.stereotype.Service;

@Service
public class CustomerService{

    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CreateCustomerRequest request){
        Customer customer = new Customer(
            request.customerId(),
            request.name()
        );
        customerRepository.save(customer);

        return customer;
    }

    public Customer getCustomer(String customerId){
        return customerRepository.findById(customerId);
    }
}