package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.models.Firestation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FirestationRepositoryTest {

    private FirestationRepository underTest;
    @BeforeEach
    void setUp() {
        underTest = new FirestationRepository();
    }

    @Test
    void itShouldReturnThreePersons() {
        // Given
        List<Firestation> firestationList;
        String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockfirestations.json";

        // When
        firestationList = underTest.getFirestations(pathToFile);

        // Then
        assertThat(firestationList).hasSize(2);
    }

    @Test
    void itShouldReturnEmptyListWhenNoData() {
        // Given
        List<Firestation> firestationList;
        String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockfirestations_empty.json";

        // When
        firestationList = underTest.getFirestations(pathToFile);

        // Then
        assertThat(firestationList).hasSize(0);

    }

}