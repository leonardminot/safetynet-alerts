package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.dto.PersonEmergencyInformationDTO;
import com.safetynet.safetynetalerts.dto.PersonsAtAddressDTO;
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
public class FloodAlertService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public FloodAlertService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<FloodAlertDTO> getFloodAlert(List<String> stationsAlert) {

        return null;
    }

    public List<String> getAddressesForStationNumber(String stationNumber) {
        List<Firestation> firestations = firestationRepository.getFirestations();
        return firestations.stream()
                .filter(firestation -> firestation.station().equals(stationNumber))
                .map(Firestation::address)
                .toList();
    }

    public PersonsAtAddressDTO getPersonsAtAddress(String address) {
        List<Person> persons = personRepository.getPersons();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        List<PersonEmergencyInformationDTO> personsLivingAtAddressEmergencyInformation = persons.stream()
                .filter(person -> person.address().equals(address))
                .map(person -> mapToPersonEmergencyInformationDTO(person, medicalRecords)).toList();
        return new PersonsAtAddressDTO(address, personsLivingAtAddressEmergencyInformation);
    }

    private PersonEmergencyInformationDTO mapToPersonEmergencyInformationDTO(Person person, List<MedicalRecord> medicalRecords) {
        return new PersonEmergencyInformationDTO(
                person.firstName(),
                person.lastName(),
                person.phone(),
                getAge(person, medicalRecords),
                getMedications(person, medicalRecords),
                getAllergies(person, medicalRecords)
        );
    }
}
