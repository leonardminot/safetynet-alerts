package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Firestation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
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
            //TODO : moche à travailler
            // deux cas à considérer :
            // - La liste est vide
            // - Le fichier n'est pas trouvé
            firestations = new ArrayList<>();
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
            // return;
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
        saveListToJson(updatedList);
    }

    public Boolean isAddressExist(Firestation firestation) {
        return getFirestations().stream()
                .anyMatch(fs -> fs.address().equals(firestation.address()));
    }

    public void deleteMapping(Firestation firestation) {
        List<Firestation> updatedList = getFirestations().stream()
                .filter(fs -> !fs.address().equals(firestation.address()))
                .toList();
        saveListToJson(updatedList);
    }

    public void deleteStation(String stationNumber) {
        List<Firestation> updatedList = getFirestations().stream()
                .filter(fs -> !fs.station().equals(stationNumber))
                .toList();
        saveListToJson(updatedList);
    }

    public boolean isStationExists(String stationNumber) {
        return getFirestations().stream()
                .anyMatch(fs -> fs.station().equals(stationNumber));
    }
}
