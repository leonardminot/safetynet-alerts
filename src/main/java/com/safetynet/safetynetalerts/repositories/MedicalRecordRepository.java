package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Repository
public class MedicalRecordRepository {

    private final String filePath;

    private final ObjectMapper objectMapper;

    @Autowired
    public MedicalRecordRepository(@Value("${safetynetalerts.jsonpath.medicalRecors}") String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
    }

    public List<MedicalRecord> getMedicalRecords() {
        List<MedicalRecord> medicalRecords;
        try {
            medicalRecords = objectMapper.readValue(Paths.get(filePath).toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            medicalRecords = Collections.emptyList();
        }
        return medicalRecords;
    }

    public void saveRecord(MedicalRecord medicalRecord) {
        List<MedicalRecord> medicalRecords = getMedicalRecords();
        medicalRecords.add(medicalRecord);
        saveListToJson(medicalRecords);
    }

    private void saveListToJson(List<MedicalRecord> medicalRecords) {
        try {
            clearJsonFile();
            fillJsonFile(medicalRecords);
        } catch (IOException e) {
            //TODO : moche, a refactoriser en intégrant la gestion des exceptions
            // return;
        }
    }

    private void clearJsonFile() throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

    private void fillJsonFile(List<MedicalRecord> medicalRecords) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), medicalRecords);
    }
}
