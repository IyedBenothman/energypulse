package com.iyed.energypulse;

public record CreateMeterReadingRequest(
    String meterId,
    double consumptionKwh
){
}