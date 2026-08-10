package com.iyed.energypulse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class CustomerController{
    
    @GetMapping("/api/customer")
    public Customer getCustomer(){

        Customer customer = new Customer("C001", "Iyed");

        customer.addReading(new MeterReading("METER001",5.2));
        customer.addReading(new MeterReading("METER001",12.8));

        return customer;
    }
}