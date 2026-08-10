package com.iyed.energypulse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CustomerController{
    
    @GetMapping("/api/customers/{customerId}")
    public Customer getCustomer(@PathVariable String customerId){

        Customer customer = new Customer(customerId, "Iyed");

        customer.addReading(new MeterReading("METER001",5.2));
        customer.addReading(new MeterReading("METER001",12.8));

        return customer;
    }

    @PostMapping("/api/customers")
    public Customer createCustomer(@RequestBody CreateCustomerRequest request){
        Customer customer = new Customer(
            request.customerId(),
            request.name()
        );

        return customer;
    }

}