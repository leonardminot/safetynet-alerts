package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.Firestation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class FirestationRepositoryTest {

    private FirestationRepository firestationRepository;

    private final String filePathMockFirestations = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockfirestations.json";

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createFirestationsMockedData(filePathMockFirestations);

        firestationRepository = new FirestationRepository(
                filePathMockFirestations,
                MyAppConfig.objectMapper());
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
        firestationList = firestationRepository.getFirestations();

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
        firestationList = firestationRepository.getFirestations();

        // Then
        assertThat(firestationList).hasSize(0);

    }

    @Test
    void itShouldCreateAMapping() {
        // Given
        Firestation firestation = new Firestation(
                "64 rue des case",
                "7"
        );

        // When
        firestationRepository.createMapping(firestation);

        // Then
        List<Firestation> allFirestations = firestationRepository.getFirestations();
        assertThat(allFirestations).hasSize(3);
        assertThat(allFirestations.get(allFirestations.size() - 1)).isEqualTo(firestation);

    }

    @Test
    void itShouldReturnAFirestationWhenAsk() {
        // Given
        Optional<Firestation> optionalFireStationThatExists;
        Optional<Firestation> optionalFireStationThatDoesntExist;

        // ... unknown firestation
        Firestation unknownFirestation = new Firestation(
                "64 rue des case",
                "7"
        );

        // ... known firestation
        Firestation knownFirestation = new Firestation(
                "007 Rue de la Dame",
                "1"
        );

        // When
        optionalFireStationThatExists = firestationRepository.isMappingExist(knownFirestation);
        optionalFireStationThatDoesntExist = firestationRepository.isMappingExist(unknownFirestation);

        // Then
        assertThat(optionalFireStationThatExists)
                .isPresent()
                .hasValueSatisfying((fs) -> assertThat(fs).isEqualTo(knownFirestation));

        assertThat(optionalFireStationThatDoesntExist).isNotPresent();
    }
}