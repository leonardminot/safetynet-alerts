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
        firestationRepository.isMappingExist(firestation).ifPresentOrElse(
                (fs) -> {
                    log.error(String.format("POST /firestation - Payload : [%s] - Error : Firestation number [%s] for address [%s] already exists", firestation.toString(), firestation.station(), firestation.address()));
                    throw new ApiResourceException(String.format("POST /firestation - Payload : [%s] - Error : Firestation number [%s] for address [%s] already exists", firestation, firestation.station(), firestation.address()));
                },
                () -> {
                    log.info(String.format("POST /firestation - Payload : [%s] - Success : Firestation number [%s] for address [%s] successfully registered",
                            firestation,
                            firestation.station(),
                            firestation.address()));
                    firestationRepository.createMapping(firestation);
                }
        );

        // TODO : vérifier le cas : on ne peut pas avoir deux stations pour une seule adress

    }

    public void updateMapping(Firestation firestation) {
        boolean isAddressExists = firestationRepository.isAddressExist(firestation);
        if (isAddressExists) {
            log.info(String.format("PUT /firestation - Payload : [%s] - Success : Firestation at address [%s] successfully updated with new station number [%s]",
                    firestation.toString(),
                    firestation.address(),
                    firestation.station()));
            firestationRepository.updateMapping(firestation);
        } else {
            log.error(String.format("PUT /firestation - Payload : [%s] - Error : No firestation found at address [%s]",
                    firestation,
                    firestation.address()));
            throw new ApiResourceException(
                    String.format("PUT /firestation - Payload : [%s] - Error : No firestation found at address [%s]",
                            firestation,
                            firestation.address()));
        }
    }

    public void deleteMapping(Firestation firestation) {
        boolean isAddressExists = firestationRepository.isAddressExist(firestation);
        if (isAddressExists) {
            log.info(String.format("DELETE /firestation - Payload : [%s] - Success : Firestation at address [%s] successfully deleted",
                    firestation.toString(),
                    firestation.address()));
            firestationRepository.deleteMapping(firestation);
        } else {
            log.error(String.format("DELETE /firestation - Payload : [%s] - Error : No firestation found at address [%s]",
                    firestation,
                    firestation.address()));
            throw new ApiResourceException(
                    String.format("DELETE /firestation - Payload : [%s] - Error : No firestation found at address [%s]",
                            firestation,
                            firestation.address()));
        }

    }

    public void deleteMapping(String address) {
        Firestation firestation = new Firestation(
                address,
                null
        );
        this.deleteMapping(firestation);
    }

    public void deleteStation(String stationNumber) {
        boolean isStationExists = firestationRepository.isStationExists(stationNumber);
        if (isStationExists) {
            log.info(
                    String.format("DELETE /firestation - Payload : {\"station\":\"%s\"} - Success : All firestations associated with station number %s successfully deleted",
                            stationNumber,
                            stationNumber));
            firestationRepository.deleteStation(stationNumber);
        } else {
            log.error(
                    String.format("DELETE /firestation - Payload : {\"station\":\"%s\"} - Error : No firestations found associated with station number %s",
                            stationNumber,
                            stationNumber));
            throw new ApiResourceException(
                    String.format("DELETE /firestation - Payload : {\"station\":\"%s\"} - Error : No firestations found associated with station number %s",
                            stationNumber,
                            stationNumber)
            );
        }

    }
}
