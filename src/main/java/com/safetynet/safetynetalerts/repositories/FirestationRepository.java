package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.models.Firestation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Repository
@Slf4j
public class FirestationRepository {

    private List<Firestation> firestations;

    public FirestationRepository() {
        this.firestations = new ArrayList<>();
    }

    public void createMapping(Firestation firestation) {
        List<Firestation> firestations = getFirestations();
        firestations.add(firestation);

        saveInitialData(firestations);
    }

    public Optional<Firestation> isMappingExist(Firestation firestation) {
        return getFirestations().stream()
                .filter(fs -> fs.address().equals(firestation.address()) && fs.station().equals(firestation.station()))
                .findAny();
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
