package com.safetynet.safetynetalerts.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.LoadInitialDataDTO;
import com.safetynet.safetynetalerts.exception.ApiRepositoryException;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InitialLoadDataService {

    private LoadInitialDataDTO dataSet;

    private final ObjectMapper objectMapper;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PersonRepository personRepository;
    private final String filePath;

    public InitialLoadDataService(
            @Value("${safetynetalerts.jsonpath.dataset}") String filePath,
            ObjectMapper objectMapper,
            FirestationRepository firestationRepository,
            MedicalRecordRepository medicalRecordRepository,
            PersonRepository personRepository) {
        this.objectMapper = objectMapper;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.personRepository = personRepository;
        this.filePath = filePath;
        this.dataSet = new LoadInitialDataDTO(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public void loadData() {
        try {
            Path path = Paths.get(this.filePath);
            if (Files.size(path) != 0)
                dataSet = objectMapper.readValue(path.toFile(), new TypeReference<>() {});

        } catch (IOException e) {
            log.error("Server ERROR - impossible to find initial dataset");
            throw new ApiRepositoryException("Server ERROR - impossible to find initial dataset");
        }
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
