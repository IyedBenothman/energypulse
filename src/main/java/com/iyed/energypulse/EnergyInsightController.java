package com.iyed.energypulse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class EnergyInsightController {

    private final EnergyInsightService energyInsightService;

    public EnergyInsightController(EnergyInsightService energyInsightService) {
        this.energyInsightService = energyInsightService;
    }

    @GetMapping("/api/customers/{customerId}/insight")
    public CustomerInsightResponse getCustomerInsight(
            @PathVariable String customerId) {

        return energyInsightService.generateCustomerInsight(customerId);
    }

}