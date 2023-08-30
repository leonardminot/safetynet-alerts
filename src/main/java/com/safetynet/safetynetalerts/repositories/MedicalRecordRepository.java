package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class MedicalRecordRepository {

    private final String filePath;

    private final ObjectMapper objectMapper;

    @Autowired
    public MedicalRecordRepository(@Value("${safetynetalerts.jsonpath.medicalRecords}") String filePath, ObjectMapper objectMapper) {
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

    public Optional<MedicalRecord> selectMedicalRecordByName(String firstName, String lastName) {
        return getMedicalRecords().stream()
                .filter(mr -> mr.firstName().equals(firstName) && mr.lastName().equals(lastName))
                .findAny();
    }

    public void update(MedicalRecord medicalRecord) {
        List<MedicalRecord> updatedList = getMedicalRecords().stream()
                .map(mr -> mr.firstName().equals(medicalRecord.firstName()) && mr.lastName().equals(medicalRecord.lastName()) ?
                        new MedicalRecord(
                                mr.firstName(),
                                mr.lastName(),
                                Objects.isNull(medicalRecord.birthdate()) ? mr.birthdate() : medicalRecord.birthdate(),
                                Objects.isNull(medicalRecord.medications()) ? mr.medications() : medicalRecord.medications(),
                                Objects.isNull(medicalRecord.allergies()) ? mr.allergies() : medicalRecord.allergies()
                        )
                        : mr)
                .toList();

        saveListToJson(updatedList);
    }

    public void delete(MedicalRecord medicalRecord) {

    }
}
