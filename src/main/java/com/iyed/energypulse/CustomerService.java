package com.iyed.energypulse;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CustomerService{

    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request){
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
        Customer savedCustomer = customerRepository.save(customer);

        return toResponse(savedCustomer);
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
            customer.getCustomerId(),
            customer.getName(),
            customer.getTotalConsumption(),
            customer.getHighConsumptionCount()
        );
    }

    public CustomerResponse getCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer " + customerId + " not found"
            ));
        return toResponse(customer);
    }

    public CustomerResponse addMeterReading(
            
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
        Customer savedCustomer = customerRepository.save(customer);

        return toResponse(savedCustomer);
    }

    public List<MeterReadingResponse> getMeterReadings(String customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer " + customerId + " not found"
                ));
        return customer.getReadings()
            .stream()
            .map(reading-> new MeterReadingResponse(
                reading.getId(),
                reading.getMeterId(),
                reading.getConsumptionKwh()
            ))
            .toList();
    }

}