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

import static com.safetynet.safetynetalerts.utils.AgeCalculation.getPersonAge;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getAllergies;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getMedications;

@Service
public class FireAlertService {
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public FireAlertService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public FireAlertDTO getPersonsAtAddress(String address) {
        List<Person> persons = personRepository.getPersons();
        List<Firestation> firestations = firestationRepository.getFirestations();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

        List<FireAlertPersonDTO> fireAlertPersons = persons.stream()
                .filter(person -> person.address().equals(address))
                .map(person -> new FireAlertPersonDTO(
                        person.firstName(),
                        person.lastName(),
                        person.phone(),
                        getPersonAge(person, medicalRecords),
                        getMedications(medicalRecords, person),
                        getAllergies(medicalRecords, person)
                )).toList();

        return new FireAlertDTO(getStationNumberAtAddress(address, firestations), fireAlertPersons);
    }

    private String getStationNumberAtAddress(String address, List<Firestation> firestations) {
        return firestations.stream()
                .filter(firestation -> firestation.address().equals(address))
                .findFirst()
                .map(Firestation::station)
                .orElse("0");
    }
}
