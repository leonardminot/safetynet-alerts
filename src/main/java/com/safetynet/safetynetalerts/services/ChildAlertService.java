package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ChildAlertService {

    private PersonRepository personRepository;
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public ChildAlertService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public long getAge(MedicalRecord medicalRecord) {
        LocalDate birthdate = medicalRecord.birthdate();
        return ChronoUnit.YEARS.between(birthdate, LocalDate.now());
    }

    public List<ChildAlertDTO> getAlertFromAddress(String address) {
        List<Person> persons = personRepository.getPersons();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

        List<Person> otherMembers = persons.stream()
                .filter(person -> person.address().equals(address))
                .filter(person -> getPersonAge(medicalRecords, person) >= 18)
                .toList();

        return persons.stream()
                .filter(person -> person.address().equals(address))
                .filter(person -> getPersonAge(medicalRecords, person) < 18)
                .map(person -> new ChildAlertDTO(
                        person.firstName(),
                        person.lastName(),
                        getPersonAge(medicalRecords, person),
                        otherMembers
                ))
                .toList();

    }

    private long getPersonAge(List<MedicalRecord> medicalRecords, Person person) {
        return medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(person.firstName()) && mr.lastName().equals(person.lastName()))
                .map(this::getAge)
                .findAny()
                .orElse(0L);
    }
}
