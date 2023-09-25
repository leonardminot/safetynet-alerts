package com.safetynet.safetynetalerts.utils;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
//TODO : en faire un service à injecter dans les classes qui en ont besoin puis créer une méthode getNow() pour pouvoir ensuite mocker
public class AgeCalculation {
    // TODO : utiliser les repositories pour les medicalRecords
    public static long calculateAgeFromMedicalRecord(MedicalRecord medicalRecord) {
        LocalDate birthdate = medicalRecord.birthdate();
        return ChronoUnit.YEARS.between(birthdate, LocalDate.now());
    }

    public static long getAge(Person person, List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(person.firstName()) && mr.lastName().equals(person.lastName()))
                .map(AgeCalculation::calculateAgeFromMedicalRecord)
                .findAny()
                .orElse(0L);
    }

    public static long getAge(PersonsCoveredByFirestationDTO personDTO, List<MedicalRecord> medicalRecords) {
        Person person = new Person(
                personDTO.firstName(),
                personDTO.lastName(),
                null,
                null,
                null,
                null,
                null
        );
        return getAge(person, medicalRecords);
    }
}
