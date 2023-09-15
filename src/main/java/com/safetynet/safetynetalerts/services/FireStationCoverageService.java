package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.safetynet.safetynetalerts.utils.AddressesResearch.getAddressesForStationNumber;
import static com.safetynet.safetynetalerts.utils.AgeCalculation.getPersonAge;

@Service
public class FireStationCoverageService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final FirestationRepository firestationRepository;

    @Autowired
    public FireStationCoverageService(PersonRepository personRepository,
                                      MedicalRecordRepository medicalRecordRepository,
                                      FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.firestationRepository = firestationRepository;
    }


    public List<PersonsCoveredByFirestationDTO> getCoverageForAStationNumber(String stationNumber) {
        List<Firestation> firestations = firestationRepository.getFirestations();
        List<String> addresses = getAddressesForStationNumber(firestations, stationNumber);
        List<Person> persons = personRepository.getPersons();

        return persons.stream()
                .filter(person -> addresses.contains(person.address()))
                .map(person -> new PersonsCoveredByFirestationDTO(
                        person.firstName(),
                        person.lastName(),
                        person.address(),
                        person.phone()
                )).toList();
    }

    public long getTotalAdults(String stationNumber) {
        List<PersonsCoveredByFirestationDTO> firestationCoverage = getCoverageForAStationNumber(stationNumber);
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

        // TODO : Gérer le cas où un dossier médical est absent
        return firestationCoverage.stream()
                .map(coverage -> getPersonAge(coverage, medicalRecords))
                .filter(age -> age >= 18)
                .count();
    }

    public long getTotalChildren(String stationNumber) {
        long totalPerson = getCoverageForAStationNumber(stationNumber).size();
        return totalPerson - getTotalAdults(stationNumber);
    }
}
