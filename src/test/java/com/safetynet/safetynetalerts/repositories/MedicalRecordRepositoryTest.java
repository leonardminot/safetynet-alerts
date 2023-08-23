package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MedicalRecordRepositoryTest {

    private MedicalRecordRepository medicalRecordRepository;

    private final String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockmedicalrecords.json";

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createMedicalRecordsMockedData(pathToFile);
        medicalRecordRepository = new MedicalRecordRepository();
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        //ManageMockedData.clearJsonFile(pathToFile);
    }

    @Test
    void itShouldReturnTwoMedicalRecords() {
        // Given
        List<MedicalRecord> medicalRecordList;

        // When
        medicalRecordList = medicalRecordRepository.getMedicalRecords(pathToFile);

        // Then
        assertThat(medicalRecordList).hasSize(2);
        System.out.println(medicalRecordList);
    }

}