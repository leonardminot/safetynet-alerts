package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.dto.FirestationStationNumberDTO;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.Person;
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
@ActiveProfiles("dev")
public class ChildAlertIT {

    @Autowired
    private MockMvc mockMvc;

    private final String filePathMockPersons;
    private final String filePathMockMedicalRecords;

    public ChildAlertIT(@Value("${safetynetalerts.jsonpath.persons}")  String filePathMockPersons,
                        @Value("${safetynetalerts.jsonpath.medicalRecords}") String filePathMockMedicalRecords) {
        this.filePathMockPersons = filePathMockPersons;
        this.filePathMockMedicalRecords = filePathMockMedicalRecords;
    }

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createPersonMockedData(filePathMockPersons);
        ManageMockedData.createMedicalRecordsMockedDataWithAllEntries(filePathMockMedicalRecords);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockPersons);
        ManageMockedData.clearJsonFile(filePathMockMedicalRecords);
    }

    @Test
    void itShouldGetTwoAdultsAndTwoChildren() throws Exception {
        // Given
        List<ChildAlertDTO> expectedResult = new ArrayList<>();

        Person maxime = new Person(
                "Maxime",
                "Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "987-654-3210",
                "maxime@email.com"
        );

        Person alireza = new Person(
                "Alireza",
                "Firouzja",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "000-111-2222",
                "alireza@email.com"
        );

        List<Person> otherMembers = new ArrayList<>();
        otherMembers.add(maxime);
        otherMembers.add(alireza);

        ChildAlertDTO miniMaxime = new ChildAlertDTO(
                "mini-Maxime",
                "mini-Vachier-Lagrave",
                3,
                otherMembers
        );

        ChildAlertDTO miniAlireza = new ChildAlertDTO(
                "mini-Alireza",
                "mini-Firouzja",
                0,
                otherMembers
        );

        expectedResult.add(miniMaxime);
        expectedResult.add(miniAlireza);

        String expectedResultString = childAlertToJson(expectedResult);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/childAlert?address=1990 Rue de la Tour")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        assert expectedResultString != null;
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(expectedResultString));
    }

    private String childAlertToJson(List<ChildAlertDTO> childAlertDTOList) {
        try {
            return new ObjectMapper().writeValueAsString(childAlertDTOList);
        } catch (JsonProcessingException e) {
            fail("");
            return null;
        }
    }
}
