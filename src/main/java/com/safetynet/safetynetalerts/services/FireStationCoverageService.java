package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        List<String> addresses = getAddressesForAStationNumber(stationNumber);
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

    private List<String> getAddressesForAStationNumber(String stationNumber) {
        List<Firestation> firestations = firestationRepository.getFirestations();
        return firestations.stream()
                .filter(fs -> fs.station().equals(stationNumber))
                .map(Firestation::address)
                .toList();
    }

    public long getTotalAdults(String stationNumber) {
        LocalDate majorDate = LocalDate.now().minusYears(18);

        List<PersonsCoveredByFirestationDTO> firestationCoverage = getCoverageForAStationNumber(stationNumber);
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

        // TODO : Gérer le cas où un dossier médical est absent
        return firestationCoverage.stream()
                .map(coverage -> getBirthDate(coverage, medicalRecords))
                .filter(birthdate -> birthdate.isBefore(majorDate) || birthdate.isEqual(majorDate))
                .count();
    }

    private LocalDate getBirthDate(PersonsCoveredByFirestationDTO personsCoveredByFirestationDTO, List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(personsCoveredByFirestationDTO.firstName()) && mr.lastName().equals(personsCoveredByFirestationDTO.lastName()))
                .map(MedicalRecord::birthdate)
                .findAny()
                .orElse(LocalDate.now());
    }


    public long getTotalChildren(String stationNumber) {
        long totalPerson = getCoverageForAStationNumber(stationNumber).size();
        return totalPerson - getTotalAdults(stationNumber);
    }
}
