package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Repository
public class PersonRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();



    public List<Person> getPersons(String filePath) throws IOException {
        return objectMapper.readValue(Paths.get(filePath).toFile(), new TypeReference<List<Person>>() {});
    }
}
