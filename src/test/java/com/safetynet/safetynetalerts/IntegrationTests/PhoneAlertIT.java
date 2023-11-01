package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.databind.JavaType;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PhoneNumbersMockedData;
import com.safetynet.safetynetalerts.services.InitialLoadDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
@ActiveProfiles("dev")
public class PhoneAlertIT {

    @Autowired
    private MockMvc mockMvc;
    private final InitialLoadDataService initialLoadDataService;

    @Value("${safetynetalerts.jsonpath.dataset}")
    private String filePath;

    @Autowired
    public PhoneAlertIT(InitialLoadDataService initialLoadDataService) {
        this.initialLoadDataService = initialLoadDataService;
    }

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createMockedDataWithAllEntries(filePath);
        initialLoadDataService.initializeData();
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePath);
        initialLoadDataService.clearData();
    }

    @Test
    void itShouldReturnAListOfPhoneNumbers() throws Exception {
        // Given
        List<String> expectedPhoneNumbers = PhoneNumbersMockedData.getPhoneNumbersForStation1();
        String stationNumber = "1";


        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert?firestation=" + stationNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JavaType listStringType = MyAppConfig.objectMapper().getTypeFactory().constructCollectionType(List.class, String.class);
        List<String> actualResult = MyAppConfig.objectMapper().readValue(jsonResponse, listStringType);

        assertThat(actualResult).containsExactlyInAnyOrderElementsOf(expectedPhoneNumbers);
    }
}
