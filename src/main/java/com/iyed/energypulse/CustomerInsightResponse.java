package com.iyed.energypulse;

public record CustomerInsightResponse(
    String customerId,
    String customerName,
    double totalKwh,
    double averageKwh,
    double maxKwh,
    int readingCount,
    int highConsumptionCount,
    String insight
) {
}