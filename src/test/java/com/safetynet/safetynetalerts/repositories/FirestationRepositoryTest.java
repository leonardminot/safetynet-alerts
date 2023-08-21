package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void itShouldReturnEmptyListWhenNoData() throws IOException {
        // Given
        List<Firestation> firestationList;
        String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockfirestations.json";

        // When
        firestationList = underTest.getFirestations(pathToFile);

        // Then
        assertThat(firestationList).hasSize(0);

    }

}