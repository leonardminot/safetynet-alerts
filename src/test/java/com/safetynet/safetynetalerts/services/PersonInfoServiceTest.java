package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.models.Person;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
public class PersonInfoServiceTest {
    private PersonInfoService personInfoService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AgeCalculationService ageCalculationService;

    @BeforeEach
    void setUp() {
        personInfoService = new PersonInfoService(personRepository, medicalRecordRepository, ageCalculationService);
    }

    @Test
    void itShouldReturnTheInfoOfMagnusCarlsen() {
        // Given
        PersonInfoDTO magnusInformation = new PersonInfoDTO(
                "Magnus",
                "Carlsen",
                32,
                "magnusd@email.com",
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());
        when(ageCalculationService.getAge(any(Person.class))).thenReturn(32L);

        // When
        List<PersonInfoDTO> actualResult = personInfoService.getPersonInfo("Magnus", "Carlsen");

        // Then
        assertThat(actualResult).isEqualTo(List.of(magnusInformation));

    }
}
