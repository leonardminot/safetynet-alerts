package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getAllergies;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getMedications;

@Service
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
        List<PersonInfoDTO> responseBody = personRepository.getPersons().stream()
                .filter(person -> person.firstName().equals(firstName) && person.lastName().equals(lastName))
                .map(this::getPersonInfoDTO)
                .toList();
        return responseBody;
    }

    private String getSuccessPersonInfoLogMess(String firstName, String lastName, List<PersonInfoDTO> responseBody) {
        return String.format(
                "GET /personInfo?firstName=%s&lastName=%s - Success: request return with body [%s]",
                firstName,
                lastName,
                responseBody
        );
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
