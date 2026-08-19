package com.iyed.energypulse;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer(){
        CreateCustomerRequest request = new CreateCustomerRequest("C001","Alex");
    
        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponse response = customerService.createCustomer(request);
    
        assertEquals("C001", response.customerId());
        assertEquals("Alex", response.name());

        verify(customerRepository).save(any(Customer.class));
    }
    @Test
    void shouldRejectDuplicateCustomer() {
        CreateCustomerRequest request =
                new CreateCustomerRequest("C001", "Alex");

        when(customerRepository.existsById("C001"))
                .thenReturn(true);

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> customerService.createCustomer(request)
                );

        assertEquals(409, exception.getStatusCode().value());
    }
    @Test
    void shouldGetExistingCustomer(){
        Customer customer = new Customer("C001", "Iyed");
        
        when(customerRepository.findById("C001"))
            .thenReturn(Optional.of(customer));
        
        CustomerResponse result = customerService.getCustomer("C001");
        
        assertEquals(result.name() , customer.getName());
        assertEquals(result.customerId(), customer.getCustomerId());

    }

    @Test
    void shouldReturnNotFoundForMissingCustomer() {
        when(customerRepository.findById("C999"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> customerService.getCustomer("C999")
                );

        assertEquals(404, exception.getStatusCode().value());
    }
    @Test
    void shouldAddMeterReadingToCustomer() {
        Customer customer = new Customer("C001", "Alex");

        when(customerRepository.findById("C001"))
                .thenReturn(Optional.of(customer));

        when(customerRepository.save(customer))
                .thenReturn(customer);

        CreateMeterReadingRequest request =
                new CreateMeterReadingRequest("METER-001", 14.6);

        CustomerResponse result =
                customerService.addMeterReading("C001", request);

        assertEquals(14.6, result.totalConsumption(), 0.0001);
        assertEquals(1, result.highConsumptionCount());
    }
    @Test
    void shouldRejectMeterReadingForMissingCustomer() {
        when(customerRepository.findById("C999"))
                .thenReturn(Optional.empty());

        CreateMeterReadingRequest request =
                new CreateMeterReadingRequest("METER-001", 14.6);

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> customerService.addMeterReading("C999", request)
                );

        assertEquals(404, exception.getStatusCode().value());
    }
}



