package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FirestationCoverageDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    public List<FirestationCoverageDTO> getCoverageForAStationNumber(String stationNumber) {
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

    public long getTotalAdults(String stationNumber) {
        LocalDate majorDate = LocalDate.now().minusYears(18);

        List<FirestationCoverageDTO> firestationCoverage = getCoverageForAStationNumber(stationNumber);
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

        // TODO : Gérer le cas où un dossier médical est absent
        return firestationCoverage.stream()
                .map(coverage -> getBirthDate(coverage, medicalRecords))
                .filter(birthdate -> birthdate.isBefore(majorDate) || birthdate.isEqual(majorDate))
                .count();
    }

    private LocalDate getBirthDate(FirestationCoverageDTO firestationCoverageDTO, List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(firestationCoverageDTO.firstName()) && mr.lastName().equals(firestationCoverageDTO.lastName()))
                .map(MedicalRecord::birthdate)
                .findAny()
                .orElse(LocalDate.now());
    }
}
