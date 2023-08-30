package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class MedicalRecordRepositoryTest {

    private MedicalRecordRepository medicalRecordRepository;

    private final String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockmedicalrecords.json";

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createMedicalRecordsMockedData(pathToFile);
        medicalRecordRepository = new MedicalRecordRepository(pathToFile, MyAppConfig.objectMapper());
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(pathToFile);
    }

    @Test
    void itShouldReturnTwoMedicalRecords() {
        // Given
        List<MedicalRecord> medicalRecordList;

        // When
        medicalRecordList = medicalRecordRepository.getMedicalRecords();

        // Then
        assertThat(medicalRecordList).hasSize(2);
    }

    @Test
    void itShouldSaveAnewRecord() {
        // Given
        MedicalRecord alirezaRecord = new MedicalRecord(
                "Alireza",
                "Firouzja",
                LocalDate.parse("2003-06-18"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );

        // When
        medicalRecordRepository.saveRecord(alirezaRecord);

        // Then
        List<MedicalRecord> allMedicalRecords = medicalRecordRepository.getMedicalRecords();
        assertThat(allMedicalRecords).hasSize(3);
        assertThat(allMedicalRecords.get(allMedicalRecords.size() - 1)).isEqualTo(alirezaRecord);

    }

    @Test
    void itShouldSelectMedicalRecordByName() {
        // Given
        Optional<MedicalRecord> optionalMedicalRecordThatExists;
        Optional<MedicalRecord> optionalMedicalRecordThatDidntExist;

        MedicalRecord magnusRecord = new MedicalRecord(
                "Magnus",
                "Carlsen",
                LocalDate.parse("1990-11-30"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );

        // When
        optionalMedicalRecordThatExists = medicalRecordRepository.selectMedicalRecordByName("Magnus", "Carlsen");
        optionalMedicalRecordThatDidntExist = medicalRecordRepository.selectMedicalRecordByName("Wesley", "So");

        // Then
        assertThat(optionalMedicalRecordThatExists)
                .isPresent()
                .hasValueSatisfying((mr) -> assertThat(mr).isEqualTo(magnusRecord));
        assertThat(optionalMedicalRecordThatDidntExist).isNotPresent();
    }

    @Test
    void itShouldUpdateAMedicalRecord() {
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
        medicalRecordRepository.update(updateRequest);

        // Then
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(currentRecord.firstName()) && mr.lastName().equals(currentRecord.lastName()))
                .findFirst();

        assertThat(medicalRecords).hasSize(2);
        assertThat(optionalMedicalRecord)
                .isPresent()
                .hasValueSatisfying(mr -> assertThat(mr).isEqualTo(finalRecord));
    }

    @Test
    void itShouldDeleteAMedicalRecord() {
        // Given
        MedicalRecord recordToDelete = new MedicalRecord(
                "Magnus",
                "Carlsen",
                null,
                null,
                null
        );

        // When
        medicalRecordRepository.delete(recordToDelete);

        // Then
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(recordToDelete.firstName()) && mr.lastName().equals(recordToDelete.lastName()))
                .findAny();
        assertThat(medicalRecords).hasSize(1);
        assertThat(optionalMedicalRecord).isNotPresent();
    }
}