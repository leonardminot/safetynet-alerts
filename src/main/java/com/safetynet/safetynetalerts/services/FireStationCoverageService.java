package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FireStationCoverageService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final FirestationRepository firestationRepository;

    @Autowired
    public FireStationCoverageService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository, FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.firestationRepository = firestationRepository;
    }
}
