package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.dto.PersonEmergencyInformationDTO;
import com.safetynet.safetynetalerts.dto.EmergencyInfoForAddressDTO;
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

import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getAllergies;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getMedications;

@Service
public class FloodAlertService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AgeCalculationService ageCalculationService;

    @Autowired
    public FloodAlertService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository, AgeCalculationService ageCalculationService) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageCalculationService = ageCalculationService;
    }

    public List<FloodAlertDTO> getFloodAlert(List<String> stationsAlert) {
        return stationsAlert.stream()
                .map(this::generateFloodAlertForGivenFireStation)
                .toList();
    }

    public FloodAlertDTO generateFloodAlertForGivenFireStation(String stationNumber) {
        List<EmergencyInfoForAddressDTO> personsCoveredByStationNumberGroupByAddress = getPersonsCoveredByStationNumberGroupByAddress(stationNumber);
        return new FloodAlertDTO(stationNumber, personsCoveredByStationNumberGroupByAddress);
    }

    private List<EmergencyInfoForAddressDTO> getPersonsCoveredByStationNumberGroupByAddress(String stationNumber) {
        return getAddressesForStationNumber(stationNumber).stream()
                .map(this::getPersonsAtAddress)
                .toList();
    }

    public List<String> getAddressesForStationNumber(String stationNumber) {
        List<Firestation> firestations = firestationRepository.getFirestations();
        return firestations.stream()
                .filter(firestation -> firestation.station().equals(stationNumber))
                .map(Firestation::address)
                .toList();
    }

    public EmergencyInfoForAddressDTO getPersonsAtAddress(String address) {
        List<Person> persons = personRepository.getPersons();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        List<PersonEmergencyInformationDTO> personsLivingAtAddressEmergencyInformation = getPersonsLivingAtGivenAddress(address, persons, medicalRecords);
        return new EmergencyInfoForAddressDTO(address, personsLivingAtAddressEmergencyInformation);
    }

    private List<PersonEmergencyInformationDTO> getPersonsLivingAtGivenAddress(String address, List<Person> persons, List<MedicalRecord> medicalRecords) {
        return persons.stream()
                .filter(person -> person.address().equals(address))
                .map(person -> mapToPersonEmergencyInformationDTO(person, medicalRecords)).toList();
    }

    private PersonEmergencyInformationDTO mapToPersonEmergencyInformationDTO(Person person, List<MedicalRecord> medicalRecords) {
        return new PersonEmergencyInformationDTO(
                person.firstName(),
                person.lastName(),
                person.phone(),
                ageCalculationService.getAge(person),
                getMedications(person, medicalRecords),
                getAllergies(person, medicalRecords)
        );
    }
}
