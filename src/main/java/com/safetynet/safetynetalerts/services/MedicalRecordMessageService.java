package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.MedicalRecord;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordMessageService {
    public MedicalRecordMessageService() {
    }

    String postSuccessLogMess(MedicalRecord medicalRecord) {
        return String.format("POST /medicalRecord - Payload: [%s] - Success: Medical record for [%s %s] successfully registered",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String postErrorPersonNotFoundLogMess(MedicalRecord medicalRecord) {
        return String.format("POST /medicalRecord - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String postErrorMedicalRecordExistsLog(MedicalRecord medicalRecord) {
        return String.format("POST /medicalRecord - Payload: [%s] - Error: Medical record for [%s %s] already exists",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String putSuccessLog(MedicalRecord medicalRecord) {
        return String.format("PUT /medicalRecord - Payload: [%s] - Success: Medical Record for [%s %s] successfully updated",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String putErrorNoMedRecordLog(MedicalRecord medicalRecord) {
        return String.format("PUT /medicalRecord - Payload: [%s] - Error: Medical Record for [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String deleteSuccessLogMess(MedicalRecord medicalRecord) {
        return String.format("DELETE /medicalRecord - Payload: [%s] - Success: Medical Record for [%s %s] successfully deleted",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    String deleteErrorNoMedRecordLogMess(MedicalRecord medicalRecord) {
        return String.format("DELETE /medicalRecord - Payload: [%s] - Error: Medical Record for [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }
}