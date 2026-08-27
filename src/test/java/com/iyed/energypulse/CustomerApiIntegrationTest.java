package com.iyed.energypulse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:energypulse-test",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@Transactional
class CustomerApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateCustomerThroughApi() throws Exception {
        mockMvc.perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "customerId": "C001",
                          "name": "Alex"
                        }
                    """)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerId").value("C001"))
            .andExpect(jsonPath("$.name").value("Alex"));
    }

    @Test
    void shouldCreateAndGetCustomerThroughApi() throws Exception{

        mockMvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "customerId": "C002",
                        "name": "Sam"
                    }
                """)
        )
            .andExpect(status().isOk());

        mockMvc.perform(
            get("/api/customers/C002")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerId").value("C002"))
            .andExpect(jsonPath("$.name").value("Sam"));
    }

    @Test
    void shouldReturnNotFoundForMissingCustomerThroughApi() throws Exception {

        mockMvc.perform(
                get("/api/customers/C999")
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Customer C999 not found"));
    }

    @Test
    void shouldRejectInvalidCustomerThroughApi() throws Exception {

        mockMvc.perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                        "customerId": "",
                        "name": ""
                        }
                    """)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.customerId").exists())
            .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    void shouldUpdateCustomerThroughApi() throws Exception {

        mockMvc.perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                        "customerId": "C003",
                        "name": "Alex"
                        }
                    """)
            )
            .andExpect(status().isOk());

        mockMvc.perform(
                patch("/api/customers/C003")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                        "name": "John"
                        }
                    """)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerId").value("C003"))
            .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void shouldDeleteCustomerThroughApi() throws Exception {

        mockMvc.perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                        "customerId": "C004",
                        "name": "Alex"
                        }
                    """)
            )
            .andExpect(status().isOk());

        mockMvc.perform(
                delete("/api/customers/C004")
            )
            .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/api/customers/C004")
            )
            .andExpect(status().isNotFound());
    }
}