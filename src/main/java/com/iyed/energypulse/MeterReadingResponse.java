package com.iyed.energypulse;

public record MeterReadingResponse(
    Long id,
    String meterId,
    double consumptionKwh
){
}