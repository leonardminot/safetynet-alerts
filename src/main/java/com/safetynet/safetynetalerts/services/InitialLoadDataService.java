package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.LoadInitialDataDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.InitialLoadDataRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InitialLoadDataService {

    private LoadInitialDataDTO dataSet;

    private final InitialLoadDataRepository initialLoadDataRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PersonRepository personRepository;

    public InitialLoadDataService(
            InitialLoadDataRepository initialLoadDataRepository,
            FirestationRepository firestationRepository,
            MedicalRecordRepository medicalRecordRepository,
            PersonRepository personRepository) {
        this.initialLoadDataRepository = initialLoadDataRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.personRepository = personRepository;
        this.dataSet = new LoadInitialDataDTO(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public void loadData() {
        dataSet = initialLoadDataRepository.loadData();
    }

    public List<Person> getPersons() {
        return dataSet.persons();
    }

    public List<MedicalRecord> getMedicalRecords() {
        return dataSet.medicalrecords();
    }

    public List<Firestation> getFirestations() {
        return dataSet.firestations();
    }

    public void savePersonsToRepository(List<Person> personsToSave) {
        personRepository.saveInitialData(personsToSave);
    }

    public void saveMedicalRecordsToRepository(List<MedicalRecord> medicalRecordsToSave) {
        medicalRecordRepository.saveInitialData(medicalRecordsToSave);
    }

    public void saveFireStationsToRepository(List<Firestation> fireStationsToSave) {
        firestationRepository.saveInitialData(fireStationsToSave);
    }

    public void initializeData() {
        loadData();
        saveMedicalRecordsToRepository(getMedicalRecords());
        saveFireStationsToRepository(getFirestations());
        savePersonsToRepository(getPersons());
    }

    public void clearData() {
        savePersonsToRepository(new ArrayList<>());
        saveMedicalRecordsToRepository(new ArrayList<>());
        saveFireStationsToRepository(new ArrayList<>());
    }

}
