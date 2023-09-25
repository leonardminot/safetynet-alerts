package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.AddressesResearch.getCoveredAddressesByFireStationNumber;

@Service
public class FireStationCoverageService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final AgeCalculation ageCalculation;

    private final int MAJORITY_AGE = 18;
    private List<Person> persons;
    private List<Firestation> firestations;


    @Autowired
    public FireStationCoverageService(PersonRepository personRepository,
                                      FirestationRepository firestationRepository, AgeCalculation ageCalculation) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.ageCalculation = ageCalculation;
        persons = List.of();
    }


    public List<PersonsCoveredByFirestationDTO> findPersonsCoveredByFirestation(String stationNumber) {
        getResourcesFromRepositories();
        List<String> addresses = getCoveredAddressesByFireStationNumber(firestations, stationNumber);
        return personsCoveredByFireStation(addresses);
    }

    private void getResourcesFromRepositories() {
        firestations = firestationRepository.getFirestations();
        persons = personRepository.getPersons();
    }

    private List<PersonsCoveredByFirestationDTO> personsCoveredByFireStation(List<String> addresses) {
        return persons.stream()
                .filter(person -> addresses.contains(person.address()))
                .map(this::transformToPersonsCoveredByFirestationDTO).toList();
    }

    private PersonsCoveredByFirestationDTO transformToPersonsCoveredByFirestationDTO(Person person) {
        return new PersonsCoveredByFirestationDTO(
                person.firstName(),
                person.lastName(),
                person.address(),
                person.phone()
        );
    }

    public long getTotalAdults(String stationNumber) {
        getResourcesFromRepositories();
        List<PersonsCoveredByFirestationDTO> firestationCoverage = findPersonsCoveredByFirestation(stationNumber);
        // TODO : Gérer le cas où un dossier médical est absent
        return countAdultsCoveredByFirestation(firestationCoverage);
    }

    private long countAdultsCoveredByFirestation(List<PersonsCoveredByFirestationDTO> firestationCoverage) {
        return firestationCoverage.stream()
                .map(ageCalculation::getAge)
                .filter(age -> age >= MAJORITY_AGE)
                .count();
    }

    public long getTotalChildren(String stationNumber) {
        long totalPerson = findPersonsCoveredByFirestation(stationNumber).size();
        return totalPerson - getTotalAdults(stationNumber);
    }
}
