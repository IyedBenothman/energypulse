package com.iyed.energypulse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnergyInsightController.class)
class EnergyInsightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnergyInsightService energyInsightService;

    @Test
    void shouldReturnCustomerInsightThroughApi() throws Exception {

        CustomerInsightResponse response = new CustomerInsightResponse(
                "C100",
                "Iyed",
                70.1,
                14.02,
                22.8,
                5,
                3,
                "Test AI insight"
        );

        when(energyInsightService.generateCustomerInsight("C100"))
                .thenReturn(response);

        mockMvc.perform(get("/api/customers/C100/insight"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C100"))
                .andExpect(jsonPath("$.totalKwh").value(70.1))
                .andExpect(jsonPath("$.readingCount").value(5))
                .andExpect(jsonPath("$.insight").value("Test AI insight"));
    }

    @Test
    void shouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {

        when(energyInsightService.generateCustomerInsight("C999"))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer C999 not found"
                ));

        mockMvc.perform(get("/api/customers/C999/insight"))
                .andExpect(status().isNotFound());
    }

}