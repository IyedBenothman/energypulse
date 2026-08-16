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

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer(){
        CreateCustomerRequest request = new CreateCustomerRequest("C001","Alex");
        Customer customer = customerService.createCustomer(request);
    
        assertEquals("C001", customer.getCustomerId());
        assertEquals("Alex", customer.getName());

        verify(customerRepository).save(customer);
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
        
        Customer result = customerService.getCustomer("C001");
        
        assertEquals(result.getName() , customer.getName());
        assertEquals(result.getCustomerId(), customer.getCustomerId());

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

        CreateMeterReadingRequest request =
                new CreateMeterReadingRequest("METER-001", 14.6);

        Customer result =
                customerService.addMeterReading("C001", request);

        assertEquals(14.6, result.getTotalConsumption(), 0.0001);
        assertEquals(1, result.getHighConsumptionCount());
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



