package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Firestation;
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
import java.util.Optional;

@Repository
public class FirestationRepository {

    private final String filePath;

    private final ObjectMapper objectMapper;

    @Autowired
    public FirestationRepository(@Value("${safetynetalerts.jsonpath.firestations}") String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
    }

    public List<Firestation> getFirestations() {
        List<Firestation> firestations;
        try {
            firestations = objectMapper.readValue(Paths.get(filePath).toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            firestations = Collections.emptyList();
        }
        return firestations;
    }

    public void createMapping(Firestation firestation) {
        List<Firestation> firestations = getFirestations();
        firestations.add(firestation);

        saveListToJson(firestations);
    }

    public Optional<Firestation> isMappingExist(Firestation firestation) {
        return getFirestations().stream()
                .filter(fs -> fs.address().equals(firestation.address()) && fs.station().equals(firestation.station()))
                .findAny();
    }

    private void saveListToJson(List<Firestation> firestations) {
        try {
            clearJsonFile();
            fillJsonFile(firestations);
        } catch (IOException e) {
            //TODO : moche, a refactoriser en intégrant la gestion des exceptions
            return;
        }
    }

    private void clearJsonFile() throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

    private void fillJsonFile(List<Firestation> firestations) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), firestations);
    }
}
