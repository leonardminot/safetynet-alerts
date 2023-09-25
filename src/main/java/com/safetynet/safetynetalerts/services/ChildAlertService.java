package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
@Slf4j
public class ChildAlertService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ChildAlertMessageService childAlertMessageService = new ChildAlertMessageService();

    private final AgeCalculationService ageCalculationService;

    List<Person> persons;
    List<MedicalRecord> medicalRecords;
    private final int MAJORITY_AGE = 18;


    @Autowired
    public ChildAlertService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository, AgeCalculationService ageCalculationService) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageCalculationService = ageCalculationService;
        this.persons = new ArrayList<>();
        this.medicalRecords = new ArrayList<>();
    }

    public List<ChildAlertDTO> getChildAlertAtAddress(String alertAddress) {
        getResourcesFromRepositories();
        List<ChildAlertDTO> listOfChildrenAtAddress = getChildrenInAlertAddress(alertAddress);
        log.info(childAlertMessageService.getSuccessChildAlertLogMess(alertAddress, listOfChildrenAtAddress));
        return listOfChildrenAtAddress;
    }

    private void getResourcesFromRepositories() {
        persons = personRepository.getPersons();
        medicalRecords = medicalRecordRepository.getMedicalRecords();
    }

    private List<ChildAlertDTO> getChildrenInAlertAddress(String alertAddress) {
        return persons.stream()
                .filter(person -> person.address().equals(alertAddress))
                .filter(isMinor())
                .map(person -> transformPersonToChildAlertDTO(person, alertAddress))
                .toList();
    }

    private Predicate<Person> isMinor() {
        return person -> ageCalculationService.getAge(person) < MAJORITY_AGE;
    }

    private ChildAlertDTO transformPersonToChildAlertDTO(Person person, String alertAddress) {
        return new ChildAlertDTO(
                person.firstName(),
                person.lastName(),
                ageCalculationService.getAge(person),
                getAdultsAtAddress(alertAddress));
    }

    private List<Person> getAdultsAtAddress(String address) {
        return persons.stream()
                .filter(person -> person.address().equals(address))
                .filter(person -> ageCalculationService.getAge(person) >= MAJORITY_AGE)
                .toList();
    }

}
