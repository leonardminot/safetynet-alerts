package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationService {

    private final FirestationRepository firestationRepository;

    @Autowired
    public FireStationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    public void createMapping(Firestation firestation) {
        firestationRepository.isMappingExist(firestation).ifPresentOrElse(
                (fs) -> {
                    throw new ApiResourceException(String.format("mapping address : [%s] with station : [%s] already exist",
                            fs.address(),
                            fs.station()));
                },
                () -> firestationRepository.createMapping(firestation)
        );

    }
}
