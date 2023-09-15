package com.safetynet.safetynetalerts.utils;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AgeCalculation {
    public static long calculateAgeFromMedicalRecord(MedicalRecord medicalRecord) {
        LocalDate birthdate = medicalRecord.birthdate();
        return ChronoUnit.YEARS.between(birthdate, LocalDate.now());
    }

    public static long getPersonAge(Person person, List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(person.firstName()) && mr.lastName().equals(person.lastName()))
                .map(AgeCalculation::calculateAgeFromMedicalRecord)
                .findAny()
                .orElse(0L);
    }

    public static long getPersonAge(PersonsCoveredByFirestationDTO personDTO, List<MedicalRecord> medicalRecords) {
        Person person = new Person(
                personDTO.firstName(),
                personDTO.lastName(),
                null,
                null,
                null,
                null,
                null
        );
        return getPersonAge(person, medicalRecords);
    }
}
