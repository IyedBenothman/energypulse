package com.iyed.energypulse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

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

    @GetMapping("/api/customers/{customerId}/readings/sorted")
    public List<MeterReadingResponse> getMeterReadingsSorted(@PathVariable String customerId){
        return customerService.getMeterReadingsSortedByConsumption(customerId);
    }

    @GetMapping("/api/customers")
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/api/customers/{customerId}/readings/page")
    public Page<MeterReadingResponse> getMeterReadingsPaginated(
            @PathVariable String customerId,
            @RequestParam int page,
            @RequestParam int size){
        
        return customerService.getMeterReadingsPaginated(
            customerId,
            page,
            size
        );
    }

    @GetMapping("/api/customers/{customerId}/readings/filter")
    public Page<MeterReadingResponse> getFilteredMeterReadings(
            @PathVariable String customerId,
            @RequestParam double minConsumption,
            @RequestParam int page,
            @RequestParam int size) {

        return customerService.getFilteredMeterReadings(
            customerId,
            minConsumption,
            page,
            size
        );
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

    @PatchMapping("/api/customers/{customerId}")
    public CustomerResponse updateCustomer(
            @PathVariable String customerId,
            @Valid @RequestBody UpdateCustomerRequest request){
        
        return customerService.updateCustomer(customerId, request);
    }

    @DeleteMapping("/api/customers/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(
            @PathVariable String customerId){
        
        customerService.deleteCustomer(customerId);
    }

}