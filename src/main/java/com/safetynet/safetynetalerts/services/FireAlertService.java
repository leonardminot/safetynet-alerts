package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertPersonDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.AgeCalculation.getAge;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getAllergies;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getMedications;

@Service
@Slf4j
public class FireAlertService {
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final FireAlertMessageService fireAlertMessageService = new FireAlertMessageService();

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalRecords;


    @Autowired
    public FireAlertService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        persons = List.of();
        firestations = List.of();
        medicalRecords = List.of();
    }

    public FireAlertDTO getFireAlertDTO(String address) {
        getResourcesFromRepositories();
        List<FireAlertPersonDTO> fireAlertPersons = getListFireAlertPersonsAtAddress(address);
        FireAlertDTO fireAlertDTO = new FireAlertDTO(getStationNumberAtAddress(address), fireAlertPersons);
        log.info(fireAlertMessageService.getSuccessFireAlertLogMess(address, fireAlertDTO));
        return fireAlertDTO;
    }

    private void getResourcesFromRepositories() {
        persons = personRepository.getPersons();
        firestations = firestationRepository.getFirestations();
        medicalRecords = medicalRecordRepository.getMedicalRecords();
    }

    private List<FireAlertPersonDTO> getListFireAlertPersonsAtAddress(String address) {
        return persons.stream()
                .filter(person -> person.address().equals(address))
                .map(this::transformPersonToFireAlertPersonDTO)
                .toList();
    }

    private FireAlertPersonDTO transformPersonToFireAlertPersonDTO(Person person) {
        return new FireAlertPersonDTO(
                person.firstName(),
                person.lastName(),
                person.phone(),
                getAge(person, medicalRecords),
                getMedications(person, medicalRecords),
                getAllergies(person, medicalRecords)
        );
    }

    private String getStationNumberAtAddress(String address) {
        return firestations.stream()
                .filter(firestation -> firestation.address().equals(address))
                .findFirst()
                .map(Firestation::station)
                .orElse("UnknownStation");
    }
}
