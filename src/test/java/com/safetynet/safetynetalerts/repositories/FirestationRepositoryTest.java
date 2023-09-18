package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.mockressources.utils.FireStationMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.Firestation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
        FireStationMockedData.createFirestationsMockedData(filePathMockFirestations);

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
        assertThat(firestationList).hasSize(3);
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
        assertThat(allFirestations).hasSize(4);
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
        Firestation knownFirestation = FireStationMockedData.getRueDeLaDame();

        // When
        optionalFireStationThatExists = firestationRepository.isMappingExist(knownFirestation);
        optionalFireStationThatDoesntExist = firestationRepository.isMappingExist(unknownFirestation);

        // Then
        assertThat(optionalFireStationThatExists)
                .isPresent()
                .hasValueSatisfying((fs) -> assertThat(fs).isEqualTo(knownFirestation));

        assertThat(optionalFireStationThatDoesntExist).isNotPresent();
    }

    @Test
    void itShouldUpdateMapping() {
        // Given
        Firestation newMapping = new Firestation(
                "007 Rue de la Dame",
                "7"
        );

        // When
        firestationRepository.updateMapping(newMapping);

        // Then
        List<Firestation> firestations = firestationRepository.getFirestations();
        assertThat(firestations).hasSize(3);

        Optional<Firestation> optionalFirestation = firestations.stream()
                .filter(fs -> fs.address().equals(newMapping.address()))
                .findAny();

        assertThat(optionalFirestation)
                .isPresent()
                .hasValueSatisfying(fs -> assertThat(fs).isEqualTo(newMapping));

    }

    @Test
    void itShouldReturnIfAddressExist() {
        // Given
        // ... unknown address
        Firestation unknownAddress = new Firestation(
                "64 rue des case",
                "7"
        );

        // ... known address
        Firestation knownAddress = FireStationMockedData.getRueDeLaDame();

        // When
        Boolean mustBeTrue = firestationRepository.isAddressExist(knownAddress);
        Boolean mustBeFalse = firestationRepository.isAddressExist(unknownAddress);

        // Then
        assertThat(mustBeTrue).isTrue();
        assertThat(mustBeFalse).isFalse();
    }

    @Test
    void itShouldIfStationExists() {
        // Given
        String knownStationNumber = "1";
        String unknownStationNumber = "7";

        // When
        Boolean mustBeTrue = firestationRepository.isStationExists(knownStationNumber);
        Boolean mustBeFalse = firestationRepository.isStationExists(unknownStationNumber);

        // Then
        assertThat(mustBeTrue).isTrue();
        assertThat(mustBeFalse).isFalse();
    }

    @Test
    void itShouldDeleteAMappingWhenFirestationIsProvided() {
        // Given
        Firestation existingAddress = new Firestation(
                "007 Rue de la Dame",
                null
        );

        // When
        firestationRepository.deleteMapping(existingAddress);

        // Then
        List<Firestation> firestations = firestationRepository.getFirestations();
        assertThat(firestations).hasSize(2);

        Optional<Firestation> optionalFirestation = firestations.stream()
                .filter(fs -> fs.address().equals(existingAddress.address()))
                .findAny();

        assertThat(optionalFirestation).isNotPresent();

    }

    @Test
    void itShouldDeleteAllMappingWithSpecificStationNumber() {
        // Given
        String stationNumber = "1";

        // When
        firestationRepository.deleteStation(stationNumber);

        // Then
        List<Firestation> firestations = firestationRepository.getFirestations();
        assertThat(firestations).hasSize(1);

        Optional<Firestation> optionalFirestation = firestations.stream()
                .filter(fs -> fs.station().equals(stationNumber))
                .findAny();

        assertThat(optionalFirestation).isNotPresent();
    }
}