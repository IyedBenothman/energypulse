package com.iyed.energypulse;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
    @NotBlank
    String customerId,
    @NotBlank
    String name
){}