package com.iyed.energypulse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateMeterReadingRequest(
    @NotBlank String meterId,
    @PositiveOrZero @NotNull Double consumptionKwh
){
}