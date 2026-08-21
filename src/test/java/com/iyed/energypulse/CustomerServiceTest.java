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
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MeterReadingRepository meterReadingRepository;

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

    @Test
    void ShouldGetMeterReading(){
        Customer customer = new Customer("C001","ALEX");
        customer.addReading(new MeterReading("METER-001",14.6));
        customer.addReading(new MeterReading("METER-002",8.2));

        when(customerRepository.findById("C001"))
                .thenReturn(Optional.of(customer));
        
        List<MeterReadingResponse> result = customerService.getMeterReadings("C001");

        assertEquals(2, result.size());

        assertEquals("METER-001", result.get(0).meterId());
        assertEquals(14.6, result.get(0).consumptionKwh(),0.0001);

        assertEquals("METER-002", result.get(1).meterId());
        assertEquals(8.2, result.get(1).consumptionKwh(),0.0001);
    }

    @Test
    void shouldReturnNotFoundWhenGettingReadingsForMissingCustomer(){
        when(customerRepository.findById("C999"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                ()-> customerService.getMeterReadings("C999")
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldGetMeterReadingsSortedByConsumption() {
        
        MeterReading reading1 = new MeterReading("METER-001", 14.6);
        MeterReading reading2 = new MeterReading("METER-002", 5.2);
        MeterReading reading3 = new MeterReading("METER-003", 22.8);
        
        when(customerRepository.existsById("C001"))
                .thenReturn(true);

        when(meterReadingRepository
                .findByCustomerCustomerIdOrderConsumptionKwhDesc("C001"))
                .thenReturn(List.of(
                        reading3,
                        reading1,
                        reading2
                ));

        List<MeterReadingResponse> result =
                customerService.getMeterReadingsSortedByConsumption("C001");

        assertEquals(3, result.size());

        assertEquals(22.8, result.get(0).consumptionKwh(), 0.0001);
        assertEquals(14.6, result.get(1).consumptionKwh(), 0.0001);
        assertEquals(5.2, result.get(2).consumptionKwh(), 0.0001);
    }
}



