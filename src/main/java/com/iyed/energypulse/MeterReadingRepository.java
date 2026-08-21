package com.iyed.energypulse;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MeterReadingRepository 
        extends JpaRepository<MeterReading, Long>{

    List<MeterReading> findByCustomerCustomerIdOrderConsumptionKwhDesc(
            String customerId
    );
}