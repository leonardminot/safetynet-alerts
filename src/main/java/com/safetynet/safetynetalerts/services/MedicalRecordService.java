package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class MedicalRecordService {

    private MedicalRecordRepository medicalRecordRepository;

    private PersonRepository personRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PersonRepository personRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.personRepository = personRepository;
    }

    public void createRecord(MedicalRecord medicalRecord) {
        throwIfMedicalRecordIsPresent(medicalRecord);
        throwIfPersonIsUnknown(medicalRecord);

        medicalRecordRepository.saveRecord(medicalRecord);
        log.info(postSuccessLog(medicalRecord));
    }

    private void throwIfPersonIsUnknown(MedicalRecord medicalRecord) {
        personRepository.selectPersonByName(medicalRecord.firstName(), medicalRecord.lastName()).orElseThrow(() -> {
                    log.error(postErrorUnknownPersonLog(medicalRecord));
                    return new ApiResourceException(postErrorUnknownPersonLog(medicalRecord));
                }
        );
    }

    private void throwIfMedicalRecordIsPresent(MedicalRecord medicalRecord) {
        medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName()).ifPresent((mr) -> {
                    log.error(postErrorMedicalRecordExistsLog(medicalRecord));
                    throw new ApiResourceException(postErrorMedicalRecordExistsLog(medicalRecord));
                });
    }

    private String postSuccessLog(MedicalRecord medicalRecord) {
        return String.format("POST /medicalRecord - Payload: [%s] - Success: Medical record for [%s %s] successfully registered",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    private String postErrorUnknownPersonLog(MedicalRecord medicalRecord) {
        return String.format("POST /medicalRecord - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    private String postErrorMedicalRecordExistsLog(MedicalRecord medicalRecord) {
        return String.format("POST /medicalRecord - Payload: [%s] - Error: Medical record for [%s %s] already exists",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    public void update(MedicalRecord medicalRecord) {
        throwIfCurrentMedicalRecordNotFound(medicalRecord);
        medicalRecordRepository.update(medicalRecord);
        log.info(putSuccessLog(medicalRecord));
    }

    private String putSuccessLog(MedicalRecord medicalRecord) {
        return String.format("PUT /medicalRecord - Payload: [%s] - Success: Medical Record for [%s %s] successfully updated",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    private void throwIfCurrentMedicalRecordNotFound(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.orElseThrow(() -> {
                    log.error(putErrorNoMedRecordLog(medicalRecord));
                    return new ApiResourceException(putErrorNoMedRecordLog(medicalRecord));
                });
    }

    private String putErrorNoMedRecordLog(MedicalRecord medicalRecord) {
        return String.format("PUT /medicalRecord - Payload: [%s] - Error: Medical Record for [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    public void delete(MedicalRecord medicalRecord) {
        throwIfMedicalRecordIsNotFound(medicalRecord);
        medicalRecordRepository.delete(medicalRecord);
        log.info(deleteSuccessLog(medicalRecord));
    }

    private void throwIfMedicalRecordIsNotFound(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.orElseThrow(() -> {
            log.error(deleteErrorNoMedRecordLog(medicalRecord));
            return new ApiResourceException(deleteErrorNoMedRecordLog(medicalRecord));
        });
    }

    private String deleteSuccessLog(MedicalRecord medicalRecord) {
        return String.format("DELETE /medicalRecord - Payload: [%s] - Success: Medical Record for [%s %s] successfully deleted",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    private String deleteErrorNoMedRecordLog(MedicalRecord medicalRecord) {
        return String.format("DELETE /medicalRecord - Payload: [%s] - Error: Medical Record for [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }
}
