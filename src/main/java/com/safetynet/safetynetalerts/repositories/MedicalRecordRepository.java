package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.models.MedicalRecord;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Repository
@Slf4j
public class MedicalRecordRepository {

    private List<MedicalRecord> medicalRecords;

    public MedicalRecordRepository() {
        this.medicalRecords = new ArrayList<>();
    }

    public void saveRecord(MedicalRecord medicalRecord) {
        List<MedicalRecord> medicalRecords = getMedicalRecords();
        medicalRecords.add(medicalRecord);
        saveInitialData(medicalRecords);
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

        saveInitialData(updatedList);
    }

    public void delete(MedicalRecord medicalRecord) {
        List<MedicalRecord> updatedList = getMedicalRecords().stream()
                .filter(mr -> !mr.firstName().equals(medicalRecord.firstName()) || !mr.lastName().equals(medicalRecord.lastName()))
                .toList();

        saveInitialData(updatedList);
    }

    public void saveInitialData(List<MedicalRecord> medicalRecordsToSave) {
        this.medicalRecords = medicalRecordsToSave;
    }
}
