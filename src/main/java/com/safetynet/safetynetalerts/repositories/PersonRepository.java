package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.models.Person;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Repository
@Slf4j
public class PersonRepository {


    private List<Person> persons;

    public PersonRepository() {
        this.persons = new ArrayList<>();
    }


    public void savePerson(Person newPerson) {
        List<Person> currentPersons = new ArrayList<>(getPersons());
        currentPersons.add(newPerson);
        saveInitialData(currentPersons);
    }

    public Optional<Person> selectPersonByName(String firstName, String lastName) {
        List<Person> persons = getPersons();
        return persons.stream().filter(p -> Objects.equals(p.firstName(), firstName) && Objects.equals(p.lastName(), lastName)).findAny();
    }

    public void update(Person person) {
        List<Person> persons = getPersons();
        List<Person> updatedPersons = persons.stream()
                .map(currentPerson -> currentPerson.firstName().equals(person.firstName()) && currentPerson.lastName().equals(person.lastName())
                        ? new Person(
                        currentPerson.firstName(),
                        currentPerson.lastName(),
                        Objects.isNull(person.address()) ? currentPerson.address() : person.address(),
                        Objects.isNull(person.city()) ? currentPerson.city() : person.city(),
                        Objects.isNull(person.zip()) ? currentPerson.zip() : person.zip(),
                        Objects.isNull(person.phone()) ? currentPerson.phone() : person.phone(),
                        Objects.isNull(person.email()) ? currentPerson.email() : person.email())
                        : currentPerson)
                .toList();
        saveInitialData(updatedPersons);
    }

    public void delete(Person person) {
        List<Person> persons = getPersons();
        List<Person> updatedPersons = persons.stream()
                .filter(p -> !p.firstName().equals(person.firstName()) || !p.lastName().equals(person.lastName()))
                .toList();
        saveInitialData(updatedPersons);
    }

    public void saveInitialData(List<Person> personsToSave) {
        persons = personsToSave;
    }
}
