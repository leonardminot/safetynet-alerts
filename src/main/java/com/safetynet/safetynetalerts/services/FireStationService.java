package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiNotFoundException;
import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FireStationService {

    private final FirestationRepository firestationRepository;
    private final FireStationMessageService fsMessageService;

    @Autowired
    public FireStationService(FirestationRepository firestationRepository, FireStationMessageService fsLog) {
        this.firestationRepository = firestationRepository;
        this.fsMessageService = fsLog;
    }

    public void createMapping(Firestation firestation) {
        throwIfFirestationExists(firestation);
        throwIfAddressHasAStation(firestation);
        firestationRepository.createMapping(firestation);
    }

    private void throwIfAddressHasAStation(Firestation firestation) {
        Boolean isAddressExist = firestationRepository.isAddressExist(firestation);
        if (isAddressExist) {
            throw new ApiResourceException(fsMessageService.postErrorAddressHasStationLogMess(firestation));
        }
    }

    private void throwIfFirestationExists(Firestation firestation) {
        firestationRepository.isMappingExist(firestation).ifPresent((fs) -> {
                    throw new ApiResourceException(fsMessageService.postErrorFirestationFoundLogMess(firestation));
                }
        );
    }

    public void updateMapping(Firestation firestation) {
        throwIfUnknownAddress(firestation, fsMessageService.putErrorFirestationNotFoundLogMess(firestation));
        firestationRepository.updateMapping(firestation);
    }

    private void throwIfUnknownAddress(Firestation firestation, String logMessage) {
        boolean isAddressExists = firestationRepository.isAddressExist(firestation);
        if (!isAddressExists) {
            throw new ApiNotFoundException(logMessage);
        }
    }

    public void deleteMapping(Firestation firestation) {
        throwIfUnknownAddress(firestation, fsMessageService.deleteErrorFirestationNotFoundLogMess(firestation));
        firestationRepository.deleteMapping(firestation);
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
        firestationRepository.deleteStation(stationNumber);
    }

    private void throwIfUnknownStationNumber(String stationNumber) {
        boolean isStationExists = firestationRepository.isStationExists(stationNumber);
        if (!isStationExists) {
            throw new ApiNotFoundException(fsMessageService.deleteErrorStationNumberNotFoundLogMess(stationNumber));
        }
    }
}
