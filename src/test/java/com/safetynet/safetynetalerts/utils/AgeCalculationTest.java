package com.safetynet.safetynetalerts.utils;

import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.services.AgeCalculation;
import com.safetynet.safetynetalerts.services.TodayDateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgeCalculationTest {

    private AgeCalculation ageCalculation;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private TodayDateService todayDateService;

    @BeforeEach
    void setUp() {
        ageCalculation = new AgeCalculation(medicalRecordRepository, todayDateService);
    }

    @Test
    void itShouldReturnTheAge() {
        // Mocked Date today: to ensure test repeatability, LocalDate.now() is mocked with the date of 2023-09-25
        // Given
        MedicalRecord adultRecord = new MedicalRecord(
                "Adult",
                "Adult",
                LocalDate.of(2005, 9, 25),
                List.of(),
                List.of()
        );

        MedicalRecord childRecord = new MedicalRecord(
                "Child",
                "Child",
                LocalDate.of(2005, 9, 26),
                List.of(),
                List.of()
        );

        when(todayDateService.getNow()).thenReturn(LocalDate.of(2023, 9, 25));

        // When
        long expectedAdultAge = ageCalculation.calculateAgeFromMedicalRecord(adultRecord);
        long expectedChildAge = ageCalculation.calculateAgeFromMedicalRecord(childRecord);

        // Then
        assertThat(expectedAdultAge).isEqualTo(18);
        assertThat(expectedChildAge).isEqualTo(17);

    }

    @Test
    void itShouldGetPersonAge() {
        // Given
        Person gari = new Person(
                "Gari",
                "Kasparov",
                "105 Rue du Fou",
                "Oslo",
                "63429",
                "741-852-9630",
                "gari@email.com"
        );

        when(medicalRecordRepository.getMedicalRecords()).thenReturn(MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries());
        when(todayDateService.getNow()).thenReturn(LocalDate.of(2023, 9, 25));

        // When
        long actualAge = ageCalculation.getAge(gari);

        // Then
        assertThat(actualAge).isEqualTo(60);

    }
}
