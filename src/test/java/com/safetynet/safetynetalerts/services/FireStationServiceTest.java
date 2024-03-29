package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiNotFoundException;
import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.mockressources.utils.FireStationMockedData;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class FireStationServiceTest {

    private FireStationService fireStationService;

    @Mock
    private FirestationRepository firestationRepository;

    @Captor
    private ArgumentCaptor<Firestation> firestationArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stationArgumentCaptor;

    @BeforeEach
    void setUp() {
        fireStationService = new FireStationService(firestationRepository, new FireStationMessageService());
    }

    @Test
    void itShouldCreateANewMapping() {
        // Given
        Firestation firestation = new Firestation(
                "64 rue des case",
                "7"
        );

        // When
        fireStationService.createMapping(firestation);

        // Then
        then(firestationRepository).should().createMapping(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue()).isEqualTo(firestation);
    }

    @Test
    void itShouldThrowWhenMappingAlreadyExists() {
        // Given an existing mapping
        Firestation rueDeLaDame = FireStationMockedData.getRueDeLaDame();

        // ... firestation exist
        when(firestationRepository.isMappingExist(any(Firestation.class))).thenReturn(Optional.of(rueDeLaDame));

        // When
        // Then
        assertThatThrownBy(() -> fireStationService.createMapping(rueDeLaDame))
                .isInstanceOf(ApiResourceException.class)
                .hasMessageContaining(
                        String.format("Impossible to create: [%s] - Error: Firestation number [%s] for address [%s] already exists",
                                rueDeLaDame,
                                rueDeLaDame.station(),
                                rueDeLaDame.address()));
        then(firestationRepository).should(never()).createMapping(any(Firestation.class));

    }

    @Test
    void itShouldThrowWhenAddressAlreadyExists() {
        // Given an existing address but with a new station number
        Firestation rueDeLaDame = new Firestation(
                "007 Rue de la Dame",
                "7" // Instead of "1" in the existing database
        );

        // ... the mapping doesn't exist
        when(firestationRepository.isMappingExist(any(Firestation.class))).thenReturn(Optional.empty());
        when(firestationRepository.isAddressExist(any(Firestation.class))).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> fireStationService.createMapping(rueDeLaDame))
                .isInstanceOf(ApiResourceException.class)
                .hasMessageContaining(
                        String.format("Impossible to create: [%s] - Error: A Firestation for address [%s] already exists",
                                rueDeLaDame,
                                rueDeLaDame.address())
                );
        then(firestationRepository).should(never()).createMapping(any(Firestation.class));
    }

    @Test
    void itShouldUpdateTheStationNumber() {
        // Given

        // ... to update to a new mapping
        Firestation futureMapping = new Firestation(
                "007 Rue de la Dame",
                "7"
        );

        when(firestationRepository.isAddressExist(any(Firestation.class))).thenReturn(true);

        // When
        fireStationService.updateMapping(futureMapping);

        // Then
        then(firestationRepository).should().updateMapping(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue()).isEqualTo(futureMapping);

    }

    @Test
    void itShouldThrowWhenAddressIsNotKnown() {
        // Given
        Firestation unknownAddress = new Firestation(
                "64 rue des case",
                "7"
        );

        when(firestationRepository.isAddressExist(any(Firestation.class))).thenReturn(false);
        // When
        // Then
        assertThatThrownBy(() -> fireStationService.updateMapping(unknownAddress))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining(String.format("Impossible to update: [%s] - Error: No firestation found at address [%s]",
                        unknownAddress,
                        unknownAddress.address()));
        then(firestationRepository).should(never()).updateMapping(any(Firestation.class));

    }

    @Test
    void itShouldDeleteAMapping() {
        // Given
        Firestation existingAddress = new Firestation(
                "007 Rue de la Dame",
                null
        );

        when(firestationRepository.isAddressExist(any(Firestation.class))).thenReturn(true);
        // When
        fireStationService.deleteMapping(existingAddress);

        // Then
        then(firestationRepository).should().deleteMapping(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue()).isEqualTo(existingAddress);

    }

    @Test
    void itShouldDeleteAMappingWithStringParameter() {
        // Given
        String existingAddress = "007 Rue de la Dame";

        when(firestationRepository.isAddressExist(any(Firestation.class))).thenReturn(true);

        // When
        fireStationService.deleteMapping(existingAddress);

        // Then
        then(firestationRepository).should().deleteMapping(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue().address()).isEqualTo(existingAddress);

    }

    @Test
    void itShouldThrowWhenAddressDoesntExistsAndNotDeleteAMapping() {
        // Given
        Firestation unknownAddress = new Firestation(
                "64 rue des case",
                "7"
        );

        when(firestationRepository.isAddressExist(any(Firestation.class))).thenReturn(false);
        // When
        // Then
        assertThatThrownBy(() -> fireStationService.deleteMapping(unknownAddress))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining(
                        String.format("Impossible to delete: [%s] - Error: No firestation found at address [%s]",
                                unknownAddress,
                                unknownAddress.address()));
        then(firestationRepository).should(never()).deleteMapping(any(Firestation.class));
    }

    @Test
    void itShouldDeleteAllAddressOfTheStationNumber() {
        // Given
        String stationNumber = "1";

        when(firestationRepository.isStationExists(any(String.class))).thenReturn(true);
        // When
        fireStationService.deleteStation(stationNumber);

        // Then
        then(firestationRepository).should().deleteStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(stationNumber);
    }

    @Test
    void itShouldThrowWhenUnknownStationNumber() {
        // Given
        String unknownStationNumber = "1";

        when(firestationRepository.isStationExists(any(String.class))).thenReturn(false);
        // When
        // Then
        assertThatThrownBy(() -> fireStationService.deleteStation(unknownStationNumber))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining(
                        String.format("Impossible to delete: {\"station\":\"%s\"} - Error: No firestations found associated with station number %s",
                                unknownStationNumber,
                                unknownStationNumber)
                );
        then(firestationRepository).should(never()).deleteStation(any(String.class));
    }
}