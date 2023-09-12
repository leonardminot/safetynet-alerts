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
        log.info(fsMessageService.postSuccessLogMessage(firestation));
    }

    private void throwIfAddressHasAStation(Firestation firestation) {
        Boolean isAddressExist = firestationRepository.isAddressExist(firestation);
        if (isAddressExist) {
            log.error(fsMessageService.postErrorAddressHasStationLogMess(firestation));
            throw new ApiResourceException(fsMessageService.postErrorAddressHasStationLogMess(firestation));
        }
    }

    private void throwIfFirestationExists(Firestation firestation) {
        firestationRepository.isMappingExist(firestation).ifPresent((fs) -> {
                    log.error(fsMessageService.postErrorFirestationFoundLogMess(firestation));
                    throw new ApiResourceException(fsMessageService.postErrorFirestationFoundLogMess(firestation));
                }
        );
    }

    public void updateMapping(Firestation firestation) {
        throwIfUnknownAddress(firestation, fsMessageService.putErrorFirestationNotFoundLogMess(firestation));
        firestationRepository.updateMapping(firestation);
        log.info(fsMessageService.putSuccessLogMess(firestation));
    }

    private void throwIfUnknownAddress(Firestation firestation, String logMessage) {
        boolean isAddressExists = firestationRepository.isAddressExist(firestation);
        if (!isAddressExists) {
            log.error(logMessage);
            throw new ApiResourceException(logMessage);
        }
    }

    public void deleteMapping(Firestation firestation) {
        throwIfUnknownAddress(firestation, fsMessageService.deleteErrorFirestationNotFoundLogMess(firestation));
        log.info(fsMessageService.deleteSuccessLogMess(firestation));
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
        log.info(fsMessageService.deleteAllStationsSuccessLogMess(stationNumber));
        firestationRepository.deleteStation(stationNumber);
    }

    private void throwIfUnknownStationNumber(String stationNumber) {
        boolean isStationExists = firestationRepository.isStationExists(stationNumber);
        if (!isStationExists) {
            log.error(fsMessageService.deleteErrorStationNumberNotFoundLogMess(stationNumber));
            throw new ApiResourceException(fsMessageService.deleteErrorStationNumberNotFoundLogMess(stationNumber));
        }
    }
}
