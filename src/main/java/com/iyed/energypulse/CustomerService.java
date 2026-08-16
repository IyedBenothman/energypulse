package com.iyed.energypulse;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerService{

    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CreateCustomerRequest request){
        String customerId = request.customerId();
        if (customerRepository.existsById(customerId)){
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Customer " + customerId + " already exists." 
            );
        }
        Customer customer = new Customer(
            customerId,
            request.name()
        );
        customerRepository.save(customer);

        return customer;
    }

    public Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer " + customerId + " not found"
            ));
    }

    public Customer addMeterReading(
            String customerId,
            CreateMeterReadingRequest request){
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer " + customerId + " not found"
            ));
        
        MeterReading reading = new MeterReading(
            request.meterId(),
            request.consumptionKwh()
        );
        
        customer.addReading(reading);
        return customer;
    }
}