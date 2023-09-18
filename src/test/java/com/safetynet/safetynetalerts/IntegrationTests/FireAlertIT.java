package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertPersonDTO;
import com.safetynet.safetynetalerts.mockressources.utils.FireStationMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
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
import java.util.List;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        String address = "1990 Rue de la Tour";

        FireAlertPersonDTO maxime = new FireAlertPersonDTO(
                "Maxime",
                "Vachier-Lagrave",
                "987-654-3210",
                32,
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of()
        );

        FireAlertPersonDTO alireza = new FireAlertPersonDTO(
                "Alireza",
                "Firouzja",
                "000-111-2222",
                20,
                List.of(),
                List.of()
        );

        FireAlertPersonDTO miniMaxime = new FireAlertPersonDTO(
                "mini-Maxime",
                "mini-Vachier-Lagrave",
                "987-654-3210",
                3,
                List.of(),
                List.of("Shellfish")
        );

        FireAlertPersonDTO miniAlireza= new FireAlertPersonDTO(
                "mini-Alireza",
                "mini-Firouzja",
                "000-111-2222",
                0,
                List.of("hydrapermazol:100mg"),
                List.of("Aspirin")
        );

        List<FireAlertPersonDTO> fireAlertPersonDTOList = List.of(maxime, alireza, miniMaxime, miniAlireza);

        FireAlertDTO expected = new FireAlertDTO("2", fireAlertPersonDTOList);
        String expectedStringResult = fireAlertToJson(expected);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/fire?address=1990 Rue de la Tour")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        assert expectedStringResult != null;
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(expectedStringResult));
    }

    // TODO : simplifier les tests d'intégration en enlevant la transformation en String <-> JSON
    private String fireAlertToJson(FireAlertDTO fireAlertDTO) {
        try {
            return new ObjectMapper().writeValueAsString(fireAlertDTO);
        } catch (JsonProcessingException e) {
            fail("");
            return null;
        }
    }
}
