package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FirestationCoverageDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FireStationCoverageService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final FirestationRepository firestationRepository;

    @Autowired
    public FireStationCoverageService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository, FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.firestationRepository = firestationRepository;
    }


    public List<FirestationCoverageDTO> getCoverage(String stationNumber) {
        List<String> addresses = getAddressesForAStationNumber(stationNumber);
        List<Person> persons = personRepository.getPersons();

        return persons.stream()
                .filter(person -> addresses.contains(person.address()))
                .map(person -> new FirestationCoverageDTO(
                        person.firstName(),
                        person.lastName(),
                        person.address(),
                        person.phone()
                )).toList();
    }

    private List<String> getAddressesForAStationNumber(String stationNumber) {
        List<Firestation> firestations = firestationRepository.getFirestations();
        return firestations.stream()
                .filter(fs -> fs.station().equals(stationNumber))
                .map(Firestation::address)
                .toList();
    }
}
