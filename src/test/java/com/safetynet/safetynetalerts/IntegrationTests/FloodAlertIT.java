package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.databind.JavaType;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.mockressources.utils.*;
import com.safetynet.safetynetalerts.services.InitialLoadDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class FloodAlertIT {
    @Autowired
    private MockMvc mockMvc;

    private final InitialLoadDataService initialLoadDataService;

    @Autowired
    public FloodAlertIT(InitialLoadDataService initialLoadDataService) {
        this.initialLoadDataService = initialLoadDataService;
    }

    @BeforeEach
    void setUp() throws IOException {
        initialLoadDataService.initializeData();
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        initialLoadDataService.clearData();
    }

    @Test
    void itShouldReturnAFloodAlert() throws Exception {
        // Given
        List<FloodAlertDTO> expectedResults = List.of(
                FloodAlertMockedData.getFloodAlertMockedDataForStation1(),
                FloodAlertMockedData.getFloodAlertMockedDataForStation2());

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations")
                        .param("stations", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JavaType listFloodAlertDTOType = MyAppConfig.objectMapper().getTypeFactory().constructCollectionType(List.class, FloodAlertDTO.class);
        List<FloodAlertDTO> actualResult = MyAppConfig.objectMapper().readValue(jsonResponse, listFloodAlertDTOType);

        assertThat(actualResult).isEqualTo(expectedResults);
    }
}
