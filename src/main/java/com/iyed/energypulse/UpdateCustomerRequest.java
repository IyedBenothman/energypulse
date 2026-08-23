package com.iyed.energypulse;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequest(
    @NotBlank String name
){
}