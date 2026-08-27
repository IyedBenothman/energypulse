package com.iyed.energypulse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.util.List;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CustomerRepositoryIntegrationTest{

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Test
    void shouldSaveAndFindCustomer(){
        Customer customer = new Customer("C001","Alex");

        customerRepository.save(customer);
        
        Customer foundCustomer = customerRepository
            .findById("C001")
            .orElseThrow();

        assertEquals("C001", foundCustomer.getCustomerId());
        assertEquals("Alex", foundCustomer.getName());
    }

    @Test
    void shouldFindCustomersWithReadings() {
        Customer customer = new Customer("C001", "Alex");

        customer.addReading(
            new MeterReading("METER-001", 14.6)
        );
        customer.addReading(
            new MeterReading("METER-002", 5.2)
        );

        customerRepository.save(customer);

        entityManager.flush();
        entityManager.clear();

        List<Customer> customers =
            customerRepository.findAllWithReadings();

        assertEquals(1, customers.size());
        assertEquals("C001", customers.get(0).getCustomerId());
        assertEquals(2, customers.get(0).getReadings().size());
    }

    @Test
    void shouldFilterAndPaginateMeterReadings() {
        Customer customer = new Customer("C001", "Alex");

        customer.addReading(new MeterReading("METER-001", 5.0));
        customer.addReading(new MeterReading("METER-002", 12.0));
        customer.addReading(new MeterReading("METER-003", 20.0));

        customerRepository.save(customer);

        entityManager.flush();
        entityManager.clear();

        Page<MeterReading> result =
            meterReadingRepository
                .findByCustomerCustomerIdAndConsumptionKwhGreaterThanEqual(
                    "C001",
                    10.0,
                    PageRequest.of(0, 2)
                );

        assertEquals(2, result.getNumberOfElements());
        assertEquals(2, result.getTotalElements());
    }
}