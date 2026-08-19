package com.iyed.energypulse;

public record CustomerResponse(
    String customerId,
    String name,
    double totalConsumption,
    int highConsumptionCount
){
}