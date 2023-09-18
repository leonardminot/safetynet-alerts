package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.databind.JavaType;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.mockressources.utils.ChildAlertMockedData;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        PersonsMockedData.createPersonMockedData(filePathMockPersons);
        MedicalRecordsMockedData.createMedicalRecordsMockedDataWithAllEntries(filePathMockMedicalRecords);
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

        expectedResult.add(ChildAlertMockedData.getMiniMaxime());
        expectedResult.add(ChildAlertMockedData.getMiniAlireza());


        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/childAlert?address=1990 Rue de la Tour")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JavaType type = MyAppConfig.objectMapper().getTypeFactory().constructCollectionType(List.class, ChildAlertDTO.class);
        List<ChildAlertDTO> actualResult = MyAppConfig.objectMapper().readValue(jsonResponse, type);
        assertThat(actualResult).containsExactlyInAnyOrderElementsOf(expectedResult);
    }
}
