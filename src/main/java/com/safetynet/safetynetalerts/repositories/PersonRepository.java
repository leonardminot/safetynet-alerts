package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.exception.ApiRepositoryException;
import com.safetynet.safetynetalerts.models.Person;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class PersonRepository {

    private final String filePath;

    private final ObjectMapper objectMapper;

    @Getter
    private List<Person> persons;

    @Autowired
    public PersonRepository(@Value("${safetynetalerts.jsonpath.persons}") String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
        this.persons = new ArrayList<>();
    }


    public void savePerson(Person newPerson) {
        List<Person> persons = getPersons();
        persons.add(newPerson);
        saveInitialData(persons);
    }

    public void saveListToJson(List<Person> persons) {
        try {
            clearJsonFile();
            fillJsonFile(persons);
        } catch (IOException e) {
            log.error("Server ERROR - impossible to find Person repository");
            throw new ApiRepositoryException("Server ERROR - impossible to find Person repository");
        }
    }

    private void clearJsonFile() throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

    private void fillJsonFile(List<Person> persons) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), persons);
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
