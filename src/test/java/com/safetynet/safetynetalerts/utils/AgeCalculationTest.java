package com.safetynet.safetynetalerts.utils;

import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.safetynet.safetynetalerts.utils.AgeCalculation.calculateAgeFromMedicalRecord;
import static com.safetynet.safetynetalerts.utils.AgeCalculation.getPersonAge;
import static org.assertj.core.api.Assertions.assertThat;

public class AgeCalculationTest {

    @Test
    void itShouldReturnTheAge() {
        // Given
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
        long expectedAdultAge = calculateAgeFromMedicalRecord(adultRecord);
        long expectedChildAge = calculateAgeFromMedicalRecord(childRecord);

        // Then
        assertThat(expectedAdultAge).isEqualTo(18);
        assertThat(expectedChildAge).isEqualTo(17);

    }

    @Test
    void itShouldGetPersonAge() {
        // Given
        List<MedicalRecord> medicalRecords = ManageMockedData.createMedicalRecordsMockedDataListWithAllEntries();

        Person gari = new Person(
                "Gari",
                "Kasparov",
                "105 Rue du Fou",
                "Oslo",
                "63429",
                "741-852-9630",
                "gari@email.com"
        );

        // When
        long actualAge = getPersonAge(gari, medicalRecords);

        // Then
        assertThat(actualAge).isEqualTo(60);

    }
}
