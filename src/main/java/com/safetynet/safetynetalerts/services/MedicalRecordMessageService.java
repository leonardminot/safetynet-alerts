package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.MedicalRecord;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordMessageService {
    public MedicalRecordMessageService() {
    }

    String postErrorPersonNotFoundLogMess(MedicalRecord medicalRecord) {
        return String.format("Impossible to create: [%s] - Error: Person with name [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String postErrorBirthdateInFutureLogMess(MedicalRecord medicalRecord) {
        return String.format("Impossible to create: [%s] - Error: Birthdate for [%s %s] is in the future: [%s]",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName(),
                medicalRecord.birthdate());
    }

    String postErrorMedicalRecordExistsLog(MedicalRecord medicalRecord) {
        return String.format("Impossible to create: [%s] - Error: Medical record for [%s %s] already exists",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String putErrorNoMedRecordLog(MedicalRecord medicalRecord) {
        return String.format("Impossible to update: [%s] - Error: Medical Record for [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String deleteErrorNoMedRecordLogMess(MedicalRecord medicalRecord) {
        return String.format("Impossible to delete: [%s] - Error: Medical Record for [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }
}