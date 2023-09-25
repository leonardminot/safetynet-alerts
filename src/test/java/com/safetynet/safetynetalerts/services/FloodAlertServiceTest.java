package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.dto.PersonEmergencyInformationDTO;
import com.safetynet.safetynetalerts.dto.EmergencyInfoForAddressDTO;
import com.safetynet.safetynetalerts.mockressources.utils.FireStationMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.FloodAlertMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
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
        TodayDateService todayDateService = new TodayDateService();
        AgeCalculationService ageCalculationService = new AgeCalculationService(medicalRecordRepository, todayDateService);
        floodAlertService = new FloodAlertService(personRepository, firestationRepository, medicalRecordRepository, new FloodAlertMessageService(), ageCalculationService);

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

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(
                MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        EmergencyInfoForAddressDTO actualRueDeLaDame = floodAlertService.getPersonsAtAddress("007 Rue de la Dame");

        // Then
        assertThat(actualRueDeLaDame.address()).isEqualTo("007 Rue de la Dame");
        assertThat(actualRueDeLaDame.personsEmergencyInformation()).containsExactlyInAnyOrder(emergencyMagnus, emergencyMiniMagnus);
    }

    @Test
    void itShouldReturnAFloodAlertDTO() {
        // Given
        FloodAlertDTO expectedAlert = FloodAlertMockedData.getFloodAlertMockedDataForStation1();
        String givenStationNumber = "1";

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(firestationRepository.getFirestations()).thenReturn(FireStationMockedData.createFirestationsMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(
                MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        FloodAlertDTO actualFloodAlert = floodAlertService.generateFloodAlertForGivenFireStation(givenStationNumber);

        // Then
        assertThat(actualFloodAlert.stationNumber()).isEqualTo(givenStationNumber);
        assertThat(actualFloodAlert.personsGroupByAddress()).hasSize(2);
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
