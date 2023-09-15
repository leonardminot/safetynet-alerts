package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChildAlertService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ChildAlertMessageService childAlertMessageService = new ChildAlertMessageService();

    List<Person> persons;
    List<MedicalRecord> medicalRecords;

    @Autowired
    public ChildAlertService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.persons = new ArrayList<>();
        this.medicalRecords = new ArrayList<>();
    }

    public List<ChildAlertDTO> getAlertFromAddress(String alertAddress) {
        persons = personRepository.getPersons();
        medicalRecords = medicalRecordRepository.getMedicalRecords();

        List<Person> adultsAtGivenAddress = getAdultsAtAddress(alertAddress);

        List<ChildAlertDTO> listOfChildrenAtAddress = persons.stream()
                .filter(person -> person.address().equals(alertAddress))
                .filter(person -> getPersonAge(person) < 18)
                .map(person -> new ChildAlertDTO(
                        person.firstName(),
                        person.lastName(),
                        getPersonAge(person),
                        adultsAtGivenAddress))
                .toList();
        log.info(childAlertMessageService.getSuccessChildAlertLogMess(alertAddress, listOfChildrenAtAddress));
        return listOfChildrenAtAddress;
    }

    private List<Person> getAdultsAtAddress(String address) {
        return persons.stream()
                .filter(person -> person.address().equals(address))
                .filter(person -> getPersonAge(person) >= 18)
                .toList();
    }

    private long getPersonAge(Person person) {
        return medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(person.firstName()) && mr.lastName().equals(person.lastName()))
                .map(this::calculateAge)
                .findAny()
                .orElse(0L);
    }

    public long calculateAge(MedicalRecord medicalRecord) {
        LocalDate birthdate = medicalRecord.birthdate();
        return ChronoUnit.YEARS.between(birthdate, LocalDate.now());
    }
}
