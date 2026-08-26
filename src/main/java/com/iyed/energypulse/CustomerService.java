package com.iyed.energypulse;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class CustomerService{

    private final CustomerRepository customerRepository;
    private final MeterReadingRepository meterReadingRepository;

    public CustomerService(
                CustomerRepository customerRepository,
                MeterReadingRepository meterReadingRepository){

            this.customerRepository = customerRepository;
            this.meterReadingRepository = meterReadingRepository;
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<MeterReadingResponse> getMeterReadingsSortedByConsumption(String customerId){
        if (!customerRepository.existsById(customerId)){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer "+ customerId + " not found"
            );
        }
        return meterReadingRepository
            .findByCustomerCustomerIdOrderByConsumptionKwhDesc(customerId)
            .stream()
            .map(reading-> new MeterReadingResponse(
                reading.getId(),
                reading.getMeterId(),
                reading.getConsumptionKwh()
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAllWithReadings()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public CustomerResponse updateCustomer(
            String customerId,
            UpdateCustomerRequest request){
        
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer "+ customerId + " not found"
            ));
        
        customer.rename(request.name());

        return toResponse(customer);
    }

    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer " + customerId + " not found"
            ));

        customerRepository.delete(customer);
    }

    @Transactional(readOnly = true)
    public Page<MeterReadingResponse> getMeterReadingsPaginated(
            String customerId,
            int page,
            int size) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer " + customerId + " not found"
            );
        }

        Pageable pageable = PageRequest.of(page, size);

        return meterReadingRepository
            .findByCustomerCustomerId(customerId, pageable)
            .map(reading -> new MeterReadingResponse(
                reading.getId(),
                reading.getMeterId(),
                reading.getConsumptionKwh()
            ));
    }

    @Transactional(readOnly = true)
    public Page<MeterReadingResponse> getFilteredMeterReadings(
            String customerId,
            double minConsumption,
            int page,
            int size) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer " + customerId + " not found"
            );
        }

        Pageable pageable = PageRequest.of(page, size);

        return meterReadingRepository
            .findByCustomerCustomerIdAndConsumptionKwhGreaterThanEqual(
                customerId,
                minConsumption,
                pageable
            )
            .map(reading -> new MeterReadingResponse(
                reading.getId(),
                reading.getMeterId(),
                reading.getConsumptionKwh()
            ));
    }

}