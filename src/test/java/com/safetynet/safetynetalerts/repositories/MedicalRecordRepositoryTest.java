package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordRepositoryTest {

    private MedicalRecordRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new MedicalRecordRepository();
    }

    @Test
    void itShouldReturnTwoMedicalRecords() {
        // Given
        List<MedicalRecord> medicalRecordList;
        String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockmedicalrecords.json";

        // When
        medicalRecordList = underTest.getMedicalRecords(pathToFile);

        // Then
        assertThat(medicalRecordList).hasSize(2);
        System.out.println(medicalRecordList);
    }

}