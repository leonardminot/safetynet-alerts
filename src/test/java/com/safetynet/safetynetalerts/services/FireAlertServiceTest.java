package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertPersonDTO;
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

        FireAlertPersonDTO maxime = new FireAlertPersonDTO(
                "Maxime",
                "Vachier-Lagrave",
                "987-654-3210",
                32,
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of()
        );

        FireAlertPersonDTO alireza = new FireAlertPersonDTO(
                "Alireza",
                "Firouzja",
                "000-111-2222",
                20,
                List.of(),
                List.of()
        );

        FireAlertPersonDTO miniMaxime = new FireAlertPersonDTO(
                "mini-Maxime",
                "mini-Vachier-Lagrave",
                "987-654-3210",
                3,
                List.of(),
                List.of("Shellfish")
        );

        FireAlertPersonDTO miniAlireza= new FireAlertPersonDTO(
                "mini-Alireza",
                "mini-Firouzja",
                "000-111-2222",
                0,
                List.of("hydrapermazol:100mg"),
                List.of("Aspirin")
        );

        List<FireAlertPersonDTO> fireAlertPersonDTOList = List.of(maxime, alireza, miniMaxime, miniAlireza);

        FireAlertDTO expected = new FireAlertDTO("2", fireAlertPersonDTOList);

        when(personRepository.getPersons()).thenReturn(ManageMockedData.createPersonMockedDataList());
        when(firestationRepository.getFirestations()).thenReturn(ManageMockedData.createFirestationsMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(ManageMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        FireAlertDTO actualResult = fireAlertService.getFireAlertDTO(address);

        // Then
        assertThat(actualResult).isEqualTo(expected);
    }
}
