package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Repository
public class PersonRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Person> getPersons(String filePath) {
        List<Person> people;
        try {
            people = objectMapper.readValue(Paths.get(filePath).toFile(), new TypeReference<>() {});

        } catch (IOException e) {
            people =  Collections.emptyList();
        }
        return people;
    }
}
