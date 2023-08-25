package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
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

    @BeforeEach
    void setUp() {
        fireStationService = new FireStationService(firestationRepository);
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
        // Given a existant mapping
        Firestation rueDeLaDame = new Firestation(
                "007 Rue de la Dame",
                "1"
        );

        // ... firestation exist
        when(firestationRepository.isMappingExist(any(Firestation.class))).thenReturn(Optional.of(rueDeLaDame));

        // When
        // Then
        assertThatThrownBy(() -> fireStationService.createMapping(rueDeLaDame))
                .isInstanceOf(ApiResourceException.class)
                .hasMessageContaining(
                        String.format("mapping address : [%s] with station : [%s] already exist",
                                rueDeLaDame.address(),
                                rueDeLaDame.station()));
        then(firestationRepository).should(never()).createMapping(any(Firestation.class));

    }

    @Test
    void itShouldUpdateTheStationNumber() {
        // Given
        // ... existant mapping
        Firestation currentMapping = new Firestation(
                "007 Rue de la Dame",
                "1"
        );

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
                .isInstanceOf(ApiResourceException.class)
                .hasMessageContaining(String.format("No mapping available for address [%s]", unknownAddress.address()));
        then(firestationRepository).should(never()).updateMapping(any(Firestation.class));

    }
}