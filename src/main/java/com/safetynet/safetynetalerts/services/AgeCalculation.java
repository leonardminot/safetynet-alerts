package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

//TODO : en faire un service à injecter dans les classes qui en ont besoin puis créer une méthode getNow() pour pouvoir ensuite mocker
@Service
public class AgeCalculation {
    private final MedicalRecordRepository medicalRecordRepository;
    private final TodayDateService todayDateService;



    @Autowired
    public AgeCalculation(MedicalRecordRepository medicalRecordRepository, TodayDateService todayDateService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.todayDateService = todayDateService;
    }

    // TODO : utiliser les repositories pour les medicalRecords
    public long calculateAgeFromMedicalRecord(MedicalRecord medicalRecord) {
        LocalDate birthdate = medicalRecord.birthdate();
        return ChronoUnit.YEARS.between(birthdate, todayDateService.getNow());
    }

    public long getAge(Person person) {
        return medicalRecordRepository.getMedicalRecords().stream()
                .filter(mr -> mr.firstName().equals(person.firstName()) && mr.lastName().equals(person.lastName()))
                .map(this::calculateAgeFromMedicalRecord)
                .findAny()
                .orElse(0L);
    }

    public long getAge(PersonsCoveredByFirestationDTO personDTO) {
        Person person = new Person(
                personDTO.firstName(),
                personDTO.lastName(),
                null,
                null,
                null,
                null,
                null
        );
        return getAge(person);
    }
}
