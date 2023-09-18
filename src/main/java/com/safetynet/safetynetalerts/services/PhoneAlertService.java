package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.AddressesResearch.getCoveredAddressesByFireStationNumber;

@Service
@Slf4j
public class PhoneAlertService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final PhoneAlertMessageService phoneAlertMessageService = new PhoneAlertMessageService();

    @Autowired
    public PhoneAlertService(PersonRepository personRepository, FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
    }

    public List<String> getPhoneNumbersForFireStation(String stationNumber) {
        List<Firestation> firestations = firestationRepository.getFirestations();
        List<Person> persons = personRepository.getPersons();

        List<String> addresses = getCoveredAddressesByFireStationNumber(firestations, stationNumber);

        List<String> phoneNumbersForFireStation = persons.stream()
                .filter(person -> addresses.contains(person.address()))
                .map(Person::phone)
                .toList();

        log.info(phoneAlertMessageService.getSuccessPhoneAlertLogMess(stationNumber, phoneNumbersForFireStation));
        return phoneNumbersForFireStation;
    }
}
