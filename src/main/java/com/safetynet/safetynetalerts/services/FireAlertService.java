package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertPersonDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.AgeCalculation.getAge;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getAllergies;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getMedications;

@Service
public class FireAlertService {
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

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
        List<FireAlertPersonDTO> fireAlertPersons = getListFireAlertPersons(address);
        return new FireAlertDTO(getStationNumberAtAddress(address), fireAlertPersons);
    }

    private void getResourcesFromRepositories() {
        persons = personRepository.getPersons();
        firestations = firestationRepository.getFirestations();
        medicalRecords = medicalRecordRepository.getMedicalRecords();
    }

    private List<FireAlertPersonDTO> getListFireAlertPersons(String address) {
        return persons.stream()
                .filter(person -> person.address().equals(address))
                .map(this::getFireAlertPersonDTO)
                .toList();
    }

    private FireAlertPersonDTO getFireAlertPersonDTO(Person person) {
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
