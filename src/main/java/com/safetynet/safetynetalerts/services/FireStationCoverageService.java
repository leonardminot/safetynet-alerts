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

        LocalDate now = LocalDate.now();
        LocalDate majorDate = now.minusYears(18);

        Map<FirestationCoverageDTO, LocalDate> personWithBirthdate = new HashMap<>();

        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

        List<FirestationCoverageDTO> firestationCoverage = getCoverageForAStationNumber(stationNumber);

        for (FirestationCoverageDTO firestationCoverageDTO : firestationCoverage) {
            Optional<LocalDate> birthDate = medicalRecords.stream()
                    .filter(mr -> mr.firstName().equals(firestationCoverageDTO.firstName()) && mr.lastName().equals(firestationCoverageDTO.lastName()))
                    .map(MedicalRecord::birthdate)
                    .findAny();
            personWithBirthdate.put(firestationCoverageDTO, birthDate.orElse(LocalDate.now()));
            // TODO : Behavior when medical record is not fill to do
        }

        return personWithBirthdate.entrySet().stream()
                .filter(entry -> entry.getValue().isBefore(majorDate) || entry.getValue().isEqual(majorDate))
                .count();

    }
}
