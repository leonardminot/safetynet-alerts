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
                    log.error(String.format("On POST /firestation : mapping %s already exists", firestation.toString()));
                    throw new ApiResourceException(String.format("mapping address : [%s] with station : [%s] already exist",
                            fs.address(),
                            fs.station()));
                },
                () -> {
                    log.info(String.format("On POST /firestation : Success for the creation of the mapping %s", firestation.toString()));
                    firestationRepository.createMapping(firestation);
                }
        );

    }

    public void updateMapping(Firestation firestation) {
        boolean isAddressExists = firestationRepository.isAddressExist(firestation);
        if (isAddressExists) {
            log.info(String.format("On PUT /firestation : Success for the update of the mapping %s", firestation.toString()));
            firestationRepository.updateMapping(firestation);
        } else {
            log.error(String.format("On PUT /firestation : No mapping available for address [%s]", firestation.address()));
            throw new ApiResourceException(
                    String.format("No mapping available for address [%s]", firestation.address()));
        }
    }

    public void deleteMapping(Firestation firestation) {
        boolean isAddressExists = firestationRepository.isAddressExist(firestation);
        if (isAddressExists) {
            log.info(String.format("On DELETE /firestation : Success for delete mapping %s", firestation.toString()));
            firestationRepository.deleteMapping(firestation);
        } else {
            log.error(String.format("On DELETE /firestation : Impossible to delete Mapping : unknown adress [%s]", firestation.address()));
            throw new ApiResourceException(
                    String.format("Impossible to delete Mapping : unknown adress [%s]", firestation.address()));
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
                    String.format("On DELETE /firestation : Success for delete all address with station number %s", stationNumber));
            firestationRepository.deleteStation(stationNumber);
        } else {
            log.error(
                    String.format("On DELETE /firestation : Impossible to delete station [%s] : no station with this number found", stationNumber));
            throw new ApiResourceException(
                    String.format("Impossible to delete station [%s] : no station with this number found", stationNumber)
            );
        }

    }
}
