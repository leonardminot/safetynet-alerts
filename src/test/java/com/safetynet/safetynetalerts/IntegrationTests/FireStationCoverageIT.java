package com.safetynet.safetynetalerts.IntegrationTests;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.dto.FirestationStationNumberDTO;
import com.safetynet.safetynetalerts.mockressources.utils.*;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class FireStationCoverageIT {

    @Autowired
    private MockMvc mockMvc;

    private final String filePathMockFireStations;
    private final String filePathMockPersons;
    private final String filePathMockMedicalRecords;

    public FireStationCoverageIT(@Value("${safetynetalerts.jsonpath.firestations}") String filePathMockFireStations,
                                 @Value("${safetynetalerts.jsonpath.persons}") String filePathMockPersons,
                                 @Value("${safetynetalerts.jsonpath.medicalRecords}") String filePathMockMedicalRecords) {
        this.filePathMockFireStations = filePathMockFireStations;
        this.filePathMockPersons = filePathMockPersons;
        this.filePathMockMedicalRecords = filePathMockMedicalRecords;
    }

    @BeforeEach
    void setUp() throws IOException {
        PersonsMockedData.createPersonMockedData(filePathMockPersons);
        FireStationMockedData.createFirestationsMockedData(filePathMockFireStations);
        MedicalRecordsMockedData.createMedicalRecordsMockedData(filePathMockMedicalRecords);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockPersons);
        ManageMockedData.clearJsonFile(filePathMockFireStations);
        ManageMockedData.clearJsonFile(filePathMockMedicalRecords);
    }

    @Test
    void itShouldGet3PersonsAndReturn2AdultsAnd1Child() throws Exception {
        // Given
        FirestationStationNumberDTO expectedResponseBody = FireStationCoverageMockedData.getMockedData();

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/firestation?stationNumber=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        FirestationStationNumberDTO actual = MyAppConfig.objectMapper().readValue(jsonResponse, FirestationStationNumberDTO.class);

        assertThat(actual.totalAdults()).isEqualTo(expectedResponseBody.totalAdults());
        assertThat(actual.totalChildren()).isEqualTo(expectedResponseBody.totalChildren());
        assertThat(actual.personsCovered()).containsExactlyInAnyOrderElementsOf(expectedResponseBody.personsCovered());
    }
}
