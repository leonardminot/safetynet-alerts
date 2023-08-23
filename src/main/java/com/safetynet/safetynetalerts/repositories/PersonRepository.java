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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
            persons =  Collections.emptyList();
        }
        return persons;
    }

    public void savePerson(Person newPerson) {
        List<Person> persons = getPersons();
        persons.add(newPerson);
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

    public Optional<Person> selectCustomerByName(String firstName, String lastName) {
        List<Person> persons = getPersons();
        return persons.stream()
                .filter(p -> Objects.equals(p.firstName(), firstName) && Objects.equals(p.lastName(), lastName))
                .findAny();
    }
}
