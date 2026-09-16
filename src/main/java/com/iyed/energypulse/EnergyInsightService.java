package com.iyed.energypulse;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.Message;
import software.amazon.awssdk.core.exception.SdkException;

@Service
public class EnergyInsightService {

    private final BedrockRuntimeClient bedrockRuntimeClient;
    private final CustomerRepository customerRepository;
    private final MeterReadingRepository meterReadingRepository;

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public EnergyInsightService(
            BedrockRuntimeClient bedrockRuntimeClient,
            CustomerRepository customerRepository,
            MeterReadingRepository meterReadingRepository) {

        this.bedrockRuntimeClient = bedrockRuntimeClient;
        this.customerRepository = customerRepository;
        this.meterReadingRepository = meterReadingRepository;
    }

    public CustomerInsightResponse generateCustomerInsight(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer " + customerId + " not found"
                ));

        var readings =
                meterReadingRepository
                        .findByCustomerCustomerIdOrderByConsumptionKwhDesc(customerId);

        double totalKwh = readings.stream()
                .mapToDouble(MeterReading::getConsumptionKwh)
                .sum();

        double averageKwh = readings.stream()
                .mapToDouble(MeterReading::getConsumptionKwh)
                .average()
                .orElse(0.0);

        double maxKwh = readings.stream()
                .mapToDouble(MeterReading::getConsumptionKwh)
                .max()
                .orElse(0.0);

        int readingCount = readings.size();

        int highConsumptionCount = (int) readings.stream()
                .filter(MeterReading::isHighConsumption)
                .count();

        totalKwh = roundTwoDecimals(totalKwh);
        averageKwh = roundTwoDecimals(averageKwh);
        maxKwh = roundTwoDecimals(maxKwh);

        String prompt = """
                Customer: %s
                Total consumption: %.2f kWh
                Average reading: %.2f kWh
                Maximum reading: %.2f kWh
                Number of readings: %d
                High-consumption readings (>10 kWh): %d

                Generate a short energy-consumption insight for this customer.
                Do not invent any numbers.
                Use only the information provided.
                Give one practical recommendation.
                """.formatted(
                    customer.getName(),
                    totalKwh,
                    averageKwh,
                    maxKwh,
                    readingCount,
                    highConsumptionCount
                );

        String insight = generateInsight(prompt);

        return new CustomerInsightResponse(
                customer.getCustomerId(),
                customer.getName(),
                totalKwh,
                averageKwh,
                maxKwh,
                readingCount,
                highConsumptionCount,
                insight
        );
    }

    public String generateInsight(String prompt) {

        Message message = Message.builder()
                .role(ConversationRole.USER)
                .content(ContentBlock.fromText(prompt))
                .build();

        ConverseRequest request = ConverseRequest.builder()
                .modelId("eu.amazon.nova-2-lite-v1:0")
                .messages(message)
                .inferenceConfig(config -> config
                        .maxTokens(300)
                        .temperature(0.2F))
                .build();

        ConverseResponse response;

        try {
        response = bedrockRuntimeClient.converse(request);
        } catch (SdkException exception) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "AI insight service is temporarily unavailable"
        );
        }
        
        return response.output()
                .message()
                .content()
                .get(0)
                .text();
    }
}