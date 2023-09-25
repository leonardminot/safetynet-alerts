package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getAllergies;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getMedications;

public class PersonInfoService {

    private final PersonRepository personRepository;

    private final MedicalRecordRepository medicalRecordRepository;

    private final AgeCalculationService ageCalculationService;

    public PersonInfoService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository, AgeCalculationService ageCalculationService) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageCalculationService = ageCalculationService;
    }

    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) {
        return personRepository.getPersons().stream()
                .filter(person -> person.firstName().equals(firstName) && person.lastName().equals(lastName))
                .map(this::getPersonInfoDTO)
                .toList();
    }

    private PersonInfoDTO getPersonInfoDTO(Person person) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        return new PersonInfoDTO(
                person.firstName(),
                person.lastName(),
                ageCalculationService.getAge(person),
                person.email(),
                getMedications(person, medicalRecords),
                getAllergies(person, medicalRecords)
        );
    }
}
