package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("dev")
public class PhoneAlertIT {

    @Autowired
    private MockMvc mockMvc;

    private final String filePathMockPersons;
    private final String filePathMockFireStations;

    public PhoneAlertIT(@Value("${safetynetalerts.jsonpath.persons}") String filePathMockPersons,
                        @Value("${safetynetalerts.jsonpath.firestations}") String filePathMockFireStations) {
        this.filePathMockPersons = filePathMockPersons;
        this.filePathMockFireStations = filePathMockFireStations;
    }

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createPersonMockedData(filePathMockPersons);
        ManageMockedData.createFirestationsMockedData(filePathMockFireStations);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockPersons);
        ManageMockedData.clearJsonFile(filePathMockFireStations);
    }

    @Test
    void itShouldReturnAListOfPhoneNumbers() throws Exception {
        // Given
        List<String> expectedPhoneNumbers = new ArrayList<>(List.of("123-456-7890", "741-852-9630"));
        expectedPhoneNumbers.add(null);
        String stationNumber = "1";

        String expectedResultString = phoneNumbersToJson(expectedPhoneNumbers);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert?firestation=" + stationNumber)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        assert expectedResultString != null;
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(expectedResultString));
    }

    private String phoneNumbersToJson(List<String> phoneNumbers) {
        try {
            return new ObjectMapper().writeValueAsString(phoneNumbers);
        } catch (JsonProcessingException e) {
            fail("");
            return null;
        }
    }
}
