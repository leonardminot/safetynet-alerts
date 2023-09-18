package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.mockressources.utils.*;
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
import static org.mockito.BDDMockito.given;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
public class FireStationCoverageServiceTest {
    private FireStationCoverageService fireStationCoverageService;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        fireStationCoverageService = new FireStationCoverageService(
                personRepository,
                medicalRecordRepository,
                firestationRepository);
    }

    @Test
    void itShouldThreePersonsForFireStation1() {
        // Given
        String stationNumber = "1";
        PersonsCoveredByFirestationDTO magnus = FireStationCoverageMockedData.getMagnus();

        PersonsCoveredByFirestationDTO miniMagnus = FireStationCoverageMockedData.getMiniMagnus();

        PersonsCoveredByFirestationDTO gari = FireStationCoverageMockedData.getGari();

        given(personRepository.getPersons()).willReturn(PersonsMockedData.createPersonMockedDataList());
        given(firestationRepository.getFirestations()).willReturn(FireStationMockedData.createFirestationsMockedDataList());

        // When
        List<PersonsCoveredByFirestationDTO> firestationCoverageList = fireStationCoverageService.findPersonsCoveredByFirestation(stationNumber);

        // Then
        assertThat(firestationCoverageList).hasSize(3);
        assertThat(firestationCoverageList).contains(magnus).contains(gari).contains(miniMagnus);
    }

    @Test
    void itShouldReturn2Adults() {
        // Given
        String stationNumber = "1";
        given(personRepository.getPersons()).willReturn(PersonsMockedData.createPersonMockedDataList());
        given(firestationRepository.getFirestations()).willReturn(FireStationMockedData.createFirestationsMockedDataList());
        given(medicalRecordRepository.getMedicalRecords()).willReturn(MedicalRecordsMockedData.createMedicalRecordsMockedDataList());

        // When
        long adults = fireStationCoverageService.getTotalAdults(stationNumber);

        // Then
        assertThat(adults).isEqualTo(2);

    }

    @Test
    void itShouldReturn1Child() {
        // Given
        String stationNumber = "1";
        given(personRepository.getPersons()).willReturn(PersonsMockedData.createPersonMockedDataList());
        given(firestationRepository.getFirestations()).willReturn(FireStationMockedData.createFirestationsMockedDataList());
        given(medicalRecordRepository.getMedicalRecords()).willReturn(MedicalRecordsMockedData.createMedicalRecordsMockedDataList());

        // When
        long childs = fireStationCoverageService.getTotalChildren(stationNumber);

        // Then
        assertThat(childs).isEqualTo(1);

    }
}
