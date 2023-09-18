package com.safetynet.safetynetalerts.IntegrationTests;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.dto.FireAlertDTO;
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
public class FireAlertIT {

    @Autowired
    private MockMvc mockMvc;

    private final String filePathMockPersons;
    private final String filePathMockFirestations;
    private final String filePathMockMedicalRecords;

    public FireAlertIT(@Value("${safetynetalerts.jsonpath.persons}") String filePathMockPersons,
                       @Value("${safetynetalerts.jsonpath.firestations}") String filePathMockFirestations,
                       @Value("${safetynetalerts.jsonpath.medicalRecords}") String filePathMockMedicalRecords) {
        this.filePathMockPersons = filePathMockPersons;
        this.filePathMockFirestations = filePathMockFirestations;
        this.filePathMockMedicalRecords = filePathMockMedicalRecords;
    }

    @BeforeEach
    void setUp() throws IOException {
        PersonsMockedData.createPersonMockedData(filePathMockPersons);
        FireStationMockedData.createFirestationsMockedData(filePathMockFirestations);
        MedicalRecordsMockedData.createMedicalRecordsMockedDataWithAllEntries(filePathMockMedicalRecords);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockPersons);
        ManageMockedData.clearJsonFile(filePathMockFirestations);
        ManageMockedData.clearJsonFile(filePathMockMedicalRecords);
    }

    @Test
    void itShouldGetAFireAlert() throws Exception {
        // Given
        FireAlertDTO expected = FireAlertMockedData.getMockedData();

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/fire?address=1990 Rue de la Tour")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        FireAlertDTO actual = MyAppConfig.objectMapper().readValue(jsonResponse, FireAlertDTO.class);

        assertThat(actual.FireStationNumber()).isEqualTo(expected.FireStationNumber());
        assertThat(actual.personsAtAddress()).containsExactlyInAnyOrderElementsOf(expected.personsAtAddress());
    }
}
