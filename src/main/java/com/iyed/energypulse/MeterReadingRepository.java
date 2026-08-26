package com.iyed.energypulse;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeterReadingRepository 
        extends JpaRepository<MeterReading, Long>{

    List<MeterReading> findByCustomerCustomerIdOrderByConsumptionKwhDesc(
            String customerId
    );

    Page<MeterReading> findByCustomerCustomerId(
        String customerId,
        Pageable pageable
    );

    Page<MeterReading> findByCustomerCustomerIdAndConsumptionKwhGreaterThanEqual(
        String customerId,
        double minConsumption,
        Pageable pageable
    );
}