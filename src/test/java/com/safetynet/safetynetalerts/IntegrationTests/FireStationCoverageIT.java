package com.safetynet.safetynetalerts.IntegrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
}
