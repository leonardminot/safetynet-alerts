package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.mockressources.utils.ChildAlertMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
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
public class ChildAlertServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    private ChildAlertService childAlertService;

    @BeforeEach
    void setUp() {
        TodayDateService todayDateService = new TodayDateService();
        AgeCalculation ageCalculation = new AgeCalculation(medicalRecordRepository, todayDateService);
        childAlertService = new ChildAlertService(personRepository, medicalRecordRepository, ageCalculation);
    }


    @Test
    void itShouldReturnTwoChildrenAndTwoAdults() {
        // Given
        // ... Children living at "1990 rue de la Tour"
        ChildAlertDTO miniMaxime = ChildAlertMockedData.getMiniMaxime();
        ChildAlertDTO miniAlireza = ChildAlertMockedData.getMiniAlireza();

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        List<ChildAlertDTO> actualResult = childAlertService.getChildAlertAtAddress("1990 Rue de la Tour");

        // Then
        System.out.println(actualResult);
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult).contains(miniMaxime).contains(miniAlireza);

    }

    @Test
    void itShouldReturnAnEmptyListWhenNoChildATheAddress() {
        // Given

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());

        // When
        List<ChildAlertDTO> actualResult = childAlertService.getChildAlertAtAddress("2023 unknown address");

        // Then
        assertThat(actualResult).hasSize(0);

    }
}
