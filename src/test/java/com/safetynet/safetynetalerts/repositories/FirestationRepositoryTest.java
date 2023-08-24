package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.Firestation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class FirestationRepositoryTest {

    private FirestationRepository firestationRepository;

    private final String filePathMockFirestations = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockfirestations.json";
    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createFirestationsMockedData(filePathMockFirestations);
        firestationRepository = new FirestationRepository(MyAppConfig.objectMapper());
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockFirestations);
    }

    @Test
    void itShouldReturnThreeFirestations() {
        // Given
        List<Firestation> firestationList;

        // When
        firestationList = firestationRepository.getFirestations(filePathMockFirestations);

        // Then
        assertThat(firestationList).hasSize(2);
    }

    @Test
    void itShouldReturnEmptyListWhenNoData() throws FileNotFoundException {
        // Given
        List<Firestation> firestationList;
        // ... an empty json file
        ManageMockedData.clearJsonFile(filePathMockFirestations);

        // When
        firestationList = firestationRepository.getFirestations(filePathMockFirestations);

        // Then
        assertThat(firestationList).hasSize(0);

    }

}