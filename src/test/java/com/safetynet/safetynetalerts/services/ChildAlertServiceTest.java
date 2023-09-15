package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
public class ChildAlertServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    private ChildAlertService childAlertService;

    @BeforeEach
    void setUp() {
        childAlertService = new ChildAlertService(personRepository, medicalRecordRepository);
    }

    @Test
    void itShouldReturnTheAge() {
        // Given
        LocalDate today = LocalDate.of(2023, 9, 15);

        MedicalRecord adultRecord = new MedicalRecord(
                "Adult",
                "Adult",
                LocalDate.of(2005, 9, 15),
                List.of(),
                List.of()
        );

        MedicalRecord childRecord = new MedicalRecord(
                "Child",
                "Child",
                LocalDate.of(2005, 9, 16),
                List.of(),
                List.of()
        );

        // When
        long expectedAdultAge = childAlertService.getAge(adultRecord);
        long expectedChildAge = childAlertService.getAge(childRecord);

        // Then
        assertThat(expectedAdultAge).isEqualTo(18);
        assertThat(expectedChildAge).isEqualTo(17);

    }

    @Test
    void itShouldReturnTwoChildrenAndTwoAdults() {
        // Given
        List<ChildAlertDTO> expectedResult = new ArrayList<>();

        Person maxime = new Person(
                "Maxime",
                "Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "987-654-3210",
                "maxime@email.com"
        );

        Person alireza = new Person(
                "Alireza",
                "Firouzja",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "1990 Rue de la Tour",
                "alireza@email.com"
        );

        List<Person> otherMembers = new ArrayList<>();
        otherMembers.add(maxime);
        otherMembers.add(alireza);

        ChildAlertDTO miniMaxime = new ChildAlertDTO(
                "mini-Maxime",
                "mini-Vachier-Lagrave",
                3,
                otherMembers
        );

        ChildAlertDTO miniAlireza = new ChildAlertDTO(
                "mini-Alireza",
                "mini-Firouzja",
                0,
                otherMembers
        );

        expectedResult.add(miniMaxime);
        expectedResult.add(miniAlireza);

        when(personRepository.getPersons()).thenReturn(ManageMockedData.createPersonMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(ManageMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        List<ChildAlertDTO> actualResult = childAlertService.getAlertFromAddress("1990 Rue de la Tour");

        // Then
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult).contains(miniMaxime).contains(miniAlireza);

    }
}
