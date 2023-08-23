package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Firestation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Repository
public class FirestationRepository {

    private final ObjectMapper objectMapper;

    @Autowired
    public FirestationRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Firestation> getFirestations(String pathToFile) {
        List<Firestation> firestations;
        try {
            firestations = objectMapper.readValue(Paths.get(pathToFile).toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            firestations = Collections.emptyList();
        }
        return firestations;
    }
}
