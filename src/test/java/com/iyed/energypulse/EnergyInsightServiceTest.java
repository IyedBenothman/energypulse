package com.iyed.energypulse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnergyInsightServiceTest {

    @Mock
    private BedrockRuntimeClient bedrockRuntimeClient;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MeterReadingRepository meterReadingRepository;

    @InjectMocks
    private EnergyInsightService energyInsightService;

    @Test
    void shouldReturnNotFoundForMissingCustomer() {

        when(customerRepository.findById("C999"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> energyInsightService.generateCustomerInsight("C999")
        );

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Customer C999 not found", exception.getReason());
    }

    @Test
    void shouldReturnServiceUnavailableWhenBedrockFails() {

        when(bedrockRuntimeClient.converse(any(ConverseRequest.class)))
                .thenThrow(SdkClientException.builder()
                        .message("Bedrock unavailable")
                        .build());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> energyInsightService.generateInsight("test prompt")
        );

        assertEquals(503, exception.getStatusCode().value());
        assertEquals(
                "AI insight service is temporarily unavailable",
                exception.getReason()
        );
    }


}

