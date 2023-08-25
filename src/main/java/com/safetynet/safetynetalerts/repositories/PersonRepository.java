package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

@Repository
public class PersonRepository {

    private final String filePath;

    private final ObjectMapper objectMapper;

    @Autowired
    public PersonRepository(@Value("${safetynetalerts.jsonpath.persons}") String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
    }

    public List<Person> getPersons() {
        List<Person> persons;
        try {
            persons = objectMapper.readValue(Paths.get(filePath).toFile(), new TypeReference<>() {});

        } catch (IOException e) {
            //TODO : moche à travailler
            // deux cas à considérer :
            // - La liste est vide
            // - Le fichier n'est pas trouvé
            persons =  new ArrayList<>();
        }
        return persons;
    }

    public void savePerson(Person newPerson) {
        List<Person> persons = getPersons();
        persons.add(newPerson);
        saveListToJson(persons);
    }

    private void saveListToJson(List<Person> persons) {
        try {
            clearJsonFile();
            fillJsonFile(persons);
        } catch (IOException e) {
            //TODO : moche, a refactoriser en intégrant la gestion des exceptions
            return;
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
        return persons.stream()
                .filter(p -> Objects.equals(p.firstName(), firstName) && Objects.equals(p.lastName(), lastName))
                .findAny();
    }

    public void update(Person person) {
        List<Person> persons = getPersons();
        List<Person> updatedList = persons.stream()
                .map(currentPerson -> currentPerson.firstName().equals(person.firstName()) && currentPerson.lastName().equals(person.lastName()) ?
                        new Person(
                                currentPerson.firstName(),
                                currentPerson.lastName(),
                                Objects.isNull(person.address()) ? currentPerson.address() : person.address(),
                                Objects.isNull(person.city()) ? currentPerson.city() : person.city(),
                                Objects.isNull(person.zip()) ? currentPerson.zip() : person.zip(),
                                Objects.isNull(person.phone()) ? currentPerson.phone() : person.phone(),
                                Objects.isNull(person.email()) ? currentPerson.email() : person.email())
                        : currentPerson)
                .toList();
        saveListToJson(updatedList);
    }
}
