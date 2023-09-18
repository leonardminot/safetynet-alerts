package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.dto.PersonEmergencyInformationDTO;
import com.safetynet.safetynetalerts.dto.PersonsAtAddressDTO;
import com.safetynet.safetynetalerts.mockressources.utils.*;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
public class FloodAlertServiceTest {

    private FloodAlertService floodAlertService;

    @Mock
    private PersonRepository personRepository;
    @Mock
    private FirestationRepository firestationRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    void setUp() {
        floodAlertService = new FloodAlertService(personRepository, firestationRepository, medicalRecordRepository);

    }

    @Test
    void itShouldReturnAListOfAddresses() {
        // Given
        String givenStationNumber = "1";

        when(firestationRepository.getFirestations()).thenReturn(FireStationMockedData.createFirestationsMockedDataList());
        // When
        List<String> addressesForGivenStationNumber = floodAlertService.getAddressesForStationNumber(givenStationNumber);

        // Then
        assertThat(addressesForGivenStationNumber).hasSize(2);
        assertThat(addressesForGivenStationNumber).containsExactlyInAnyOrder(
                FireStationMockedData.getRueDuFou().address(),
                FireStationMockedData.getRueDeLaDame().address());
    }

    @Test
    void itShouldReturnPersonsLivingAtRueDeLaDame() {
        // Given
        List<MedicalRecord> mockedMedicalRecords = MedicalRecordsMockedData.createMedicalRecordsMockedDataList();
        PersonEmergencyInformationDTO emergencyMiniMagnus = FloodAlertMockedData.getEmergencyMiniMagnus(mockedMedicalRecords);
        PersonEmergencyInformationDTO emergencyMagnus = FloodAlertMockedData.getEmergencyMagnus(mockedMedicalRecords);

        PersonsAtAddressDTO expectedRueDeLaDame = new PersonsAtAddressDTO(
                "007 Rue de la Dame",
                List.of(emergencyMiniMagnus, emergencyMagnus)
        );

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(
                MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        PersonsAtAddressDTO actualRueDeLaDame = floodAlertService.getPersonsAtAddress("007 Rue de la Dame");

        // Then
        assertThat(actualRueDeLaDame.address()).isEqualTo("007 Rue de la Dame");
        assertThat(actualRueDeLaDame.personsEmergencyInformation()).containsExactlyInAnyOrder(emergencyMagnus, emergencyMiniMagnus);
    }

    @Test
    void itShouldReturnAFloodAlertDTO() {
        // Given
        List<MedicalRecord> mockedMedicalRecords = MedicalRecordsMockedData.createMedicalRecordsMockedDataList();
        FloodAlertDTO expectedAlert = FloodAlertMockedData.getFloodAlertMockedDataForStation1();
        String givenStationNumber = "1";

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(firestationRepository.getFirestations()).thenReturn(FireStationMockedData.createFirestationsMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(
                MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        FloodAlertDTO actualFloodAlert = floodAlertService.generateFloodAlertForStationNumber(givenStationNumber);

        // Then
        assertThat(actualFloodAlert.stationNumber()).isEqualTo(givenStationNumber);
        assertThat(actualFloodAlert.personsAtAddress()).hasSize(2);
        assertThat(actualFloodAlert).isEqualTo(expectedAlert);
    }

    @Test
    void itShouldReturnAListOfFloodAlertDTO() {
        // Given
        List<String> alertedFireStations = List.of("1", "2");
        List<FloodAlertDTO> expectedResults = List.of(
                FloodAlertMockedData.getFloodAlertMockedDataForStation1(),
                FloodAlertMockedData.getFloodAlertMockedDataForStation2());


        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(firestationRepository.getFirestations()).thenReturn(FireStationMockedData.createFirestationsMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(
                MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        List<FloodAlertDTO> actualResults = floodAlertService.getFloodAlert(alertedFireStations);

        // Then
        assertThat(actualResults).hasSize(2);
        assertThat(actualResults).isEqualTo(expectedResults);

    }
}
