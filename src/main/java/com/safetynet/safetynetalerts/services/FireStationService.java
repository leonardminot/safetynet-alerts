package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
