package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
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
    }
}
