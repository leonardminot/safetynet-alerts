package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
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

    // TODO :
    //  1 - On recherche toutes les adresses couvertes par le numéro de la station
    //  2 - On recherche toutes les personnes couvertes par cette adresse
    //  3 - On recherche les dates de naissances
    //  4 - On compte le nombre d'enfants


    @Test
    void itShouldThreePersonsForFireStation1() {
        // Given
        String stationNumber = "1";
        PersonsCoveredByFirestationDTO magnus = new PersonsCoveredByFirestationDTO(
                "Magnus",
                "Carlsen",
                "007 Rue de la Dame",
                "123-456-7890"
        );

        PersonsCoveredByFirestationDTO miniMagnus = new PersonsCoveredByFirestationDTO(
                "miniMagnus",
                "miniCarlsen",
                "007 Rue de la Dame",
                null
        );

        PersonsCoveredByFirestationDTO gari = new PersonsCoveredByFirestationDTO(
                "Gari",
                "Kasparov",
                "105 Rue du Fou",
                "741-852-9630"
        );

        given(personRepository.getPersons()).willReturn(ManageMockedData.createPersonMockedDataList());
        given(firestationRepository.getFirestations()).willReturn(ManageMockedData.createFirestationsMockedDataList());

        // When
        List<PersonsCoveredByFirestationDTO> firestationCoverageList = fireStationCoverageService.getCoverageForAStationNumber(stationNumber);

        // Then
        assertThat(firestationCoverageList).hasSize(3);
        assertThat(firestationCoverageList).contains(magnus).contains(gari).contains(miniMagnus);
    }

    @Test
    void itShouldReturn2Adults() {
        // Given
        String stationNumber = "1";
        given(personRepository.getPersons()).willReturn(ManageMockedData.createPersonMockedDataList());
        given(firestationRepository.getFirestations()).willReturn(ManageMockedData.createFirestationsMockedDataList());
        given(medicalRecordRepository.getMedicalRecords()).willReturn(ManageMockedData.createMedicalRecordsMockedDataList());

        // When
        long adults = fireStationCoverageService.getTotalAdults(stationNumber);

        // Then
        assertThat(adults).isEqualTo(2);

    }

    @Test
    void itShouldReturn1Child() {
        // Given
        String stationNumber = "1";
        given(personRepository.getPersons()).willReturn(ManageMockedData.createPersonMockedDataList());
        given(firestationRepository.getFirestations()).willReturn(ManageMockedData.createFirestationsMockedDataList());
        given(medicalRecordRepository.getMedicalRecords()).willReturn(ManageMockedData.createMedicalRecordsMockedDataList());

        // When
        long childs = fireStationCoverageService.getTotalChildren(stationNumber);

        // Then
        assertThat(childs).isEqualTo(1);

    }
}
