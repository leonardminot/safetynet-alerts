package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.FirestationStationNumberDTO;
import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.Firestation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        ManageMockedData.createPersonMockedData(filePathMockPersons);
        ManageMockedData.createFirestationsMockedData(filePathMockFireStations);
        ManageMockedData.createMedicalRecordsMockedData(filePathMockMedicalRecords);
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
        PersonsCoveredByFirestationDTO magnus = new PersonsCoveredByFirestationDTO(
                "Magnus",
                "Carlsen",
                "007 Rue de la Dame",
                "123-456-7890"
        );

        PersonsCoveredByFirestationDTO miniMagnus = new PersonsCoveredByFirestationDTO(
                "miniMagnus",
                "miniCarlsen",
                "007 Rue de la Dame",
                null
        );

        PersonsCoveredByFirestationDTO gari = new PersonsCoveredByFirestationDTO(
                "Gari",
                "Kasparov",
                "105 Rue du Fou",
                "741-852-9630"
        );

        List<PersonsCoveredByFirestationDTO> personsCoveredByFirestationDTOS = new ArrayList<>();
        personsCoveredByFirestationDTOS.add(magnus);
        personsCoveredByFirestationDTOS.add(gari);
        personsCoveredByFirestationDTOS.add(miniMagnus);

        FirestationStationNumberDTO responseBody = new FirestationStationNumberDTO(
                2,
                1,
                personsCoveredByFirestationDTOS
        );

        String expectedResult = personsCoveredToJson(responseBody);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/firestation?stationNumber=1")
                        .contentType(MediaType.APPLICATION_JSON));

        // Then

        resultActions.andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    private String personsCoveredToJson(FirestationStationNumberDTO personsCoveredByFirestationDTO) {
        try {
            return new ObjectMapper().writeValueAsString(personsCoveredByFirestationDTO);
        } catch (JsonProcessingException e) {
            fail("");
            return null;
        }
    }
}
