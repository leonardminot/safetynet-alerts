package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FirestationCoverageDTO;
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
    void itShouldTwoPersonsForFireStation1() {
        // Given
        String stationNumber = "1";
        FirestationCoverageDTO magnus = new FirestationCoverageDTO(
                "Magnus",
                "Carlsen",
                "007 Rue de la Dame",
                "123-456-7890"
        );

        FirestationCoverageDTO gari = new FirestationCoverageDTO(
                "Gari",
                "Kasparov",
                "105 Rue du Fou",
                "741-852-9630"
        );

        given(personRepository.getPersons()).willReturn(ManageMockedData.createPersonMockedDataList());
        given(firestationRepository.getFirestations()).willReturn(ManageMockedData.createFirestationsMockedDataList());

        // When
        List<FirestationCoverageDTO> firestationCoverageList = fireStationCoverageService.getCoverage(stationNumber);

        // Then
        assertThat(firestationCoverageList).hasSize(2);
        assertThat(firestationCoverageList).contains(magnus).contains(gari);
    }
}
