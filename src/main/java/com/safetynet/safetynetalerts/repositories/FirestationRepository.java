package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.exception.ApiRepositoryException;
import com.safetynet.safetynetalerts.models.Firestation;
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
import java.util.Optional;

@Repository
@Slf4j
public class FirestationRepository {

    private final String filePath;

    private final ObjectMapper objectMapper;
    @Getter
    private List<Firestation> firestations;

    @Autowired
    public FirestationRepository(@Value("${safetynetalerts.jsonpath.firestations}") String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
        this.firestations = new ArrayList<>();
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

    public void saveListToJson(List<Firestation> firestations) {
        try {
            clearJsonFile();
            fillJsonFile(firestations);
        } catch (IOException e) {
            log.error("Server ERROR - impossible to find Fire Station repository");
            throw new ApiRepositoryException("Server ERROR - impossible to find Fire Station repository");
        }
    }

    private void clearJsonFile() throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

    private void fillJsonFile(List<Firestation> firestations) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), firestations);
    }

    public void updateMapping(Firestation firestation) {
        List<Firestation> updatedList = getFirestations().stream()
                .map(fs -> fs.address().equals(firestation.address()) ?
                        new Firestation(
                                firestation.address(),
                                firestation.station()
                        )
                        : fs)
                .toList();
        saveInitialData(updatedList);
    }

    public Boolean isAddressExist(Firestation firestation) {
        return getFirestations().stream()
                .anyMatch(fs -> fs.address().equals(firestation.address()));
    }

    public void deleteMapping(Firestation firestation) {
        List<Firestation> updatedList = getFirestations().stream()
                .filter(fs -> !fs.address().equals(firestation.address()))
                .toList();
        saveInitialData(updatedList);
    }

    public void deleteStation(String stationNumber) {
        List<Firestation> updatedList = getFirestations().stream()
                .filter(fs -> !fs.station().equals(stationNumber))
                .toList();
        saveInitialData(updatedList);
    }

    public boolean isStationExists(String stationNumber) {
        return getFirestations().stream()
                .anyMatch(fs -> fs.station().equals(stationNumber));
    }

    public void saveInitialData(List<Firestation> firestationsToSave) {
        this.firestations = firestationsToSave;
    }
}
