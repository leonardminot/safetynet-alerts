package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class MedicalRecordIT {

    @Autowired
    private MockMvc mockMvc;

    private final String filePathMockMedicalRecords;

    private final String filePathMockPersons;

    private final MedicalRecordRepository medicalRecordRepository;

    private final PersonRepository personRepository;


    @Autowired
    public MedicalRecordIT(@Value("${safetynetalerts.jsonpath.medicalRecords}") String filePathMockMedicalRecords, @Value("${safetynetalerts.jsonpath.persons}") String filePathMockPersons, MedicalRecordRepository medicalRecordRepository, PersonRepository personRepository) {
        this.filePathMockMedicalRecords = filePathMockMedicalRecords;
        this.filePathMockPersons = filePathMockPersons;
        this.medicalRecordRepository = medicalRecordRepository;
        this.personRepository = personRepository;
    }

    @BeforeEach
    void setUp() throws IOException {
        MedicalRecordsMockedData.createMedicalRecordsMockedData(filePathMockMedicalRecords);
        PersonsMockedData.createPersonMockedData(filePathMockPersons);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockMedicalRecords);
        ManageMockedData.clearJsonFile(filePathMockPersons);
    }

    @Test
    void itShouldPostAMedicalRecord() throws Exception {
        // Given
        MedicalRecord alirezaRecord = new MedicalRecord(
                "Alireza",
                "Firouzja",
                LocalDate.parse("2003-06-18"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );

        System.out.println("Valeur de Persons.json : " + personRepository.getPersons());


        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(medicalRecordToJson(alirezaRecord))));

        // Then
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        resultActions.andExpect(status().isOk());
        assertThat(medicalRecords).hasSize(7);
        assertThat(medicalRecords.get(medicalRecords.size() - 1)).isEqualTo(alirezaRecord);
    }

    @Test
    void itShouldNotPostMedicalRecordForUnknownPerson() throws Exception {
        // Given
        MedicalRecord unknownPersonMedicalRecord = new MedicalRecord(
                "Wesley",
                "So",
                LocalDate.parse("1993-10-09"),
                null,
                null
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(medicalRecordToJson(unknownPersonMedicalRecord))));

        // Then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().is4xxClientError());
        assertThat(contentAsString).contains(
                String.format("POST /medicalRecord - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                        unknownPersonMedicalRecord,
                        unknownPersonMedicalRecord.firstName(),
                        unknownPersonMedicalRecord.lastName()));
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        assertThat(medicalRecords).hasSize(6);
    }

    @Test
    void itShouldUpdateAMedicalRecord() throws Exception {
        // Given
        // ... the current record
        MedicalRecord currentRecord = new MedicalRecord(
                "Magnus",
                "Carlsen",
                LocalDate.parse("1990-11-30"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );

        // ... the update request
        MedicalRecord updateRequest = new MedicalRecord(
                "Magnus",
                "Carlsen",
                null,
                List.of("aznol:350mg", "hydrapermazol:100mg", "ketamine:1000mg"),
                List.of("nillacilan","peanut butter")
        );

        // ... the final record after request
        MedicalRecord finalRecord = new MedicalRecord(
                "Magnus",
                "Carlsen",
                LocalDate.parse("1990-11-30"),
                List.of("aznol:350mg", "hydrapermazol:100mg", "ketamine:1000mg"),
                List.of("nillacilan","peanut butter")
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(medicalRecordToJson(updateRequest))));

        // Then
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        resultActions.andExpect(status().isOk());
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(finalRecord.firstName()) && mr.lastName().equals(finalRecord.lastName()))
                .findAny();
        assertThat(optionalMedicalRecord)
                .isPresent()
                .hasValueSatisfying(medicalRecord -> assertThat(medicalRecord).isEqualTo(finalRecord));

    }

    @Test
    void itShouldNotUpdateAMedicalRecord() throws Exception {
        // Given
        MedicalRecord unknownPersonMedicalRecord = new MedicalRecord(
                "Wesley",
                "So",
                LocalDate.parse("1993-10-09"),
                null,
                null
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(medicalRecordToJson(unknownPersonMedicalRecord))));

        // Then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().is4xxClientError());
        assertThat(contentAsString).contains(
                String.format("PUT /medicalRecord - Payload: [%s] - Error: Medical Record for [%s %s] does not exist",
                        unknownPersonMedicalRecord,
                        unknownPersonMedicalRecord.firstName(),
                        unknownPersonMedicalRecord.lastName()));
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        assertThat(medicalRecords).hasSize(6);
    }

    @Test
    void itShouldDeleteAMedicalRecord() throws Exception {
        // Given
        MedicalRecord recordToDelete = new MedicalRecord(
                "Magnus",
                "Carlsen",
                null,
                null,
                null
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(medicalRecordToJson(recordToDelete))));

        // Then
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        resultActions.andExpect(status().isOk());
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(recordToDelete.firstName()) && mr.lastName().equals(recordToDelete.lastName()))
                .findAny();
        assertThat(optionalMedicalRecord)
                .isNotPresent();
    }

    @Test
    void itShouldNotDeleteAMedicalRecord() throws Exception {
        // Given
        MedicalRecord unknownPersonMedicalRecord = new MedicalRecord(
                "Wesley",
                "So",
                LocalDate.parse("1993-10-09"),
                null,
                null
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(medicalRecordToJson(unknownPersonMedicalRecord))));

        // Then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().is4xxClientError());
        assertThat(contentAsString).contains(
                String.format("DELETE /medicalRecord - Payload: [%s] - Error: Medical Record for [%s %s] does not exist",
                        unknownPersonMedicalRecord,
                        unknownPersonMedicalRecord.firstName(),
                        unknownPersonMedicalRecord.lastName()));
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        assertThat(medicalRecords).hasSize(6);
    }

    private String medicalRecordToJson(MedicalRecord medicalRecord) {
        ObjectMapper objectMapper = MyAppConfig.objectMapper();
        try {
            return objectMapper.writeValueAsString(medicalRecord);
        } catch (JsonProcessingException e) {
            fail("");
            return null;
        }
    }
}
