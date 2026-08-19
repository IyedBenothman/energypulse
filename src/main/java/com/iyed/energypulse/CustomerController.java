package com.iyed.energypulse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import java.util.List;

@RestController
public class CustomerController{

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }
    
    @GetMapping("/api/customers/{customerId}")
    public CustomerResponse getCustomer(@PathVariable String customerId){
        return customerService.getCustomer(customerId);
    }

    @GetMapping("/api/customers/{customerId}/readings")
    public List<MeterReadingResponse> getMeterReadings(@PathVariable String customerId){
        return customerService.getMeterReadings(customerId);
    }

    @PostMapping("/api/customers")
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest request){
        
        return customerService.createCustomer(request);
    }

    @PostMapping("/api/customers/{customerId}/readings")
    public CustomerResponse addMeterReading(
            @PathVariable String customerId,
            @RequestBody @Valid CreateMeterReadingRequest request){
        
        return customerService.addMeterReading(customerId, request);
    }

}