package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FireStationService {

    private final FirestationRepository firestationRepository;

    @Autowired
    public FireStationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    public void createMapping(Firestation firestation) {
        throwIfFirestationExists(firestation);
        firestationRepository.createMapping(firestation);
        log.info(postSuccessLogMessage(firestation));
        // TODO : vérifier le cas : on ne peut pas avoir deux stations pour une seule adresse
    }

    private void throwIfFirestationExists(Firestation firestation) {
        firestationRepository.isMappingExist(firestation).ifPresent((fs) -> {
                    log.error(postErrorFirestationFoundLogMess(firestation));
                    throw new ApiResourceException(postErrorFirestationFoundLogMess(firestation));
                }
        );
    }

    private String postErrorFirestationFoundLogMess(Firestation firestation) {
        return String.format("POST /firestation - Payload: [%s] - Error: Firestation number [%s] for address [%s] already exists",
                firestation.toString(),
                firestation.station(),
                firestation.address());
    }

    private String postSuccessLogMessage(Firestation firestation) {
        return String.format("POST /firestation - Payload: [%s] - Success: Firestation number [%s] for address [%s] successfully registered",
                firestation,
                firestation.station(),
                firestation.address());
    }

    public void updateMapping(Firestation firestation) {
        throwIfUnknownAddress(firestation, putErrorFirestationNotFoundLogMess(firestation));
        firestationRepository.updateMapping(firestation);
        log.info(putSuccessLogMess(firestation));
    }

    private void throwIfUnknownAddress(Firestation firestation, String logMessage) {
        boolean isAddressExists = firestationRepository.isAddressExist(firestation);
        if (!isAddressExists) {
            log.error(logMessage);
            throw new ApiResourceException(logMessage);
        }
    }

    private String putSuccessLogMess(Firestation firestation) {
        return String.format("PUT /firestation - Payload: [%s] - Success: Firestation at address [%s] successfully updated with new station number [%s]",
                firestation.toString(),
                firestation.address(),
                firestation.station());
    }

    private String putErrorFirestationNotFoundLogMess(Firestation firestation) {
        return String.format("PUT /firestation - Payload: [%s] - Error: No firestation found at address [%s]",
                firestation,
                firestation.address());
    }

    public void deleteMapping(Firestation firestation) {
        throwIfUnknownAddress(firestation, deleteErrorFirestationNotFoundLogMess(firestation));
        log.info(deleteSuccessLogMess(firestation));
        firestationRepository.deleteMapping(firestation);
    }

    private String deleteErrorFirestationNotFoundLogMess(Firestation firestation) {
        return String.format("DELETE /firestation - Payload: [%s] - Error: No firestation found at address [%s]",
                firestation,
                firestation.address());
    }

    private String deleteSuccessLogMess(Firestation firestation) {
        return String.format("DELETE /firestation - Payload: [%s] - Success: Firestation at address [%s] successfully deleted",
                firestation.toString(),
                firestation.address());
    }

    public void deleteMapping(String address) {
        Firestation firestation = new Firestation(
                address,
                null
        );
        this.deleteMapping(firestation);
    }

    public void deleteStation(String stationNumber) {
        throwIfUnknownStationNumber(stationNumber);
        log.info(deleteAllStationsSuccessLogMess(stationNumber));
        firestationRepository.deleteStation(stationNumber);
    }

    private void throwIfUnknownStationNumber(String stationNumber) {
        boolean isStationExists = firestationRepository.isStationExists(stationNumber);
        if (!isStationExists) {
            log.error(deleteErrorStationNumberNotFoundLogMess(stationNumber));
            throw new ApiResourceException(deleteErrorStationNumberNotFoundLogMess(stationNumber));
        }
    }

    private String deleteAllStationsSuccessLogMess(String stationNumber) {
        return String.format("DELETE /firestation - Payload: {\"station\":\"%s\"} - Success: All firestations associated with station number %s successfully deleted",
                stationNumber,
                stationNumber);
    }

    private String deleteErrorStationNumberNotFoundLogMess(String stationNumber) {
        return String.format("DELETE /firestation - Payload: {\"station\":\"%s\"} - Error: No firestations found associated with station number %s",
                stationNumber,
                stationNumber);
    }
}
