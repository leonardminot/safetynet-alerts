package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public class FireStationCoverageServiceTest {
    private FireStationCoverageService fireStationCoverageService;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        fireStationCoverageService = new FireStationCoverageService(
                personRepository,
                medicalRecordRepository,
                firestationRepository);
    }


}
