package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.AddressesResearch.getAddressesForStationNumber;

@Service
public class PhoneAlertService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    @Autowired
    public PhoneAlertService(PersonRepository personRepository, FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
    }

    public List<String> getPhoneNumbersForFireStation(String stationNumber) {
        List<Firestation> firestations = firestationRepository.getFirestations();
        List<Person> persons = personRepository.getPersons();

        List<String> addresses = getAddressesForStationNumber(firestations, stationNumber);

        List<String> phoneNumbersForFireStation = persons.stream()
                .filter(person -> addresses.contains(person.address()))
                .map(Person::phone)
                .toList();


        return phoneNumbersForFireStation;
    }
}
