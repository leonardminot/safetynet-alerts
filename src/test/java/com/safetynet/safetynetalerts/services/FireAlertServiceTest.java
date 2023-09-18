package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.mockressources.utils.FireAlertMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.FireStationMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
public class FireAlertServiceTest {

    private FireAlertService fireAlertService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    void setUp() {
        this.fireAlertService = new FireAlertService(personRepository, firestationRepository, medicalRecordRepository);
    }

    @Test
    void itShouldReturnAFireAlert() {
        // Given
        String address = "1990 Rue de la Tour";
        FireAlertDTO expected = FireAlertMockedData.getMockedData();

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(firestationRepository.getFirestations()).thenReturn(FireStationMockedData.createFirestationsMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        FireAlertDTO actualResult = fireAlertService.getFireAlert(address);

        // Then
        assertThat(actualResult.FireStationNumber()).isEqualTo("2");
        assertThat(actualResult.personsAtAddress()).containsExactlyInAnyOrderElementsOf(expected.personsAtAddress());
    }
}
