package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
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
        log.info(postSuccessLogMess(medicalRecord));
    }

    private void throwIfPersonIsUnknown(MedicalRecord medicalRecord) {
        Optional<Person> optionalPerson = personRepository.selectPersonByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalPerson.orElseThrow(() -> {
                    log.error(postErrorPersonNotFoundLogMess(medicalRecord));
                    return new ApiResourceException(postErrorPersonNotFoundLogMess(medicalRecord));
                }
        );
    }

    private void throwIfMedicalRecordIsPresent(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.ifPresent((mr) -> {
            log.error(postErrorMedicalRecordExistsLog(medicalRecord));
            throw new ApiResourceException(postErrorMedicalRecordExistsLog(medicalRecord));
        });
    }

    private String postSuccessLogMess(MedicalRecord medicalRecord) {
        return String.format("POST /medicalRecord - Payload: [%s] - Success: Medical record for [%s %s] successfully registered",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    private String postErrorPersonNotFoundLogMess(MedicalRecord medicalRecord) {
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
        throwIfMedicalRecordIsNotFound(medicalRecord, putErrorNoMedRecordLog(medicalRecord));
        medicalRecordRepository.update(medicalRecord);
        log.info(putSuccessLog(medicalRecord));
    }

    private String putSuccessLog(MedicalRecord medicalRecord) {
        return String.format("PUT /medicalRecord - Payload: [%s] - Success: Medical Record for [%s %s] successfully updated",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    private void throwIfMedicalRecordIsNotFound(MedicalRecord medicalRecord, String logMessage) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.orElseThrow(() -> {
            log.error(logMessage);
            return new ApiResourceException(logMessage);
        });
    }

    private String putErrorNoMedRecordLog(MedicalRecord medicalRecord) {
        return String.format("PUT /medicalRecord - Payload: [%s] - Error: Medical Record for [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    public void delete(MedicalRecord medicalRecord) {
        throwIfMedicalRecordIsNotFound(medicalRecord, deleteErrorNoMedRecordLogMess(medicalRecord));
        medicalRecordRepository.delete(medicalRecord);
        log.info(deleteSuccessLogMess(medicalRecord));
    }

    private String deleteSuccessLogMess(MedicalRecord medicalRecord) {
        return String.format("DELETE /medicalRecord - Payload: [%s] - Success: Medical Record for [%s %s] successfully deleted",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }

    private String deleteErrorNoMedRecordLogMess(MedicalRecord medicalRecord) {
        return String.format("DELETE /medicalRecord - Payload: [%s] - Error: Medical Record for [%s %s] does not exist",
                medicalRecord,
                medicalRecord.firstName(),
                medicalRecord.lastName());
    }
}
