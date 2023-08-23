package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Repository
public class MedicalRecordRepository {

    private final ObjectMapper objectMapper;

    @Autowired
    public MedicalRecordRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<MedicalRecord> getMedicalRecords(String pathToFile) {
        List<MedicalRecord> medicalRecords;
        try {
            medicalRecords = objectMapper.readValue(Paths.get(pathToFile).toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            medicalRecords = Collections.emptyList();
        }
        return medicalRecords;
    }
}
