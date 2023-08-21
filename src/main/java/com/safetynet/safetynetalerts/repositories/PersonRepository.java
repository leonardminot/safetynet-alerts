package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
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
    public PersonRepository(@Value("${safetynetalerts.jsonpath}") String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
    }

    public List<Person> getPersons() {
        List<Person> people;
        try {
            people = objectMapper.readValue(Paths.get(filePath).toFile(), new TypeReference<>() {});

        } catch (IOException e) {
            people =  Collections.emptyList();
        }
        return people;
    }

    public void savePerson(Person capture) {
    }

    public Optional<Person> selectCustomerByName(String firstName, String lastName) {
        List<Person> persons = getPersons();
        return persons.stream()
                .filter(p -> Objects.equals(p.firstName(), firstName) && Objects.equals(p.lastName(), lastName))
                .findAny();
    }
}
