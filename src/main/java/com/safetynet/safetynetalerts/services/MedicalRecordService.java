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

    private final MedicalRecordMessageService messService;
    private final MedicalRecordRepository medicalRecordRepository;

    private final PersonRepository personRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordMessageService messService, MedicalRecordRepository medicalRecordRepository, PersonRepository personRepository) {
        this.messService = messService;
        this.medicalRecordRepository = medicalRecordRepository;
        this.personRepository = personRepository;
    }

    public void createRecord(MedicalRecord medicalRecord) {
        // TODO : it should throw when birthdate is in the future
        throwIfMedicalRecordIsPresent(medicalRecord);
        throwIfPersonIsUnknown(medicalRecord);
        medicalRecordRepository.saveRecord(medicalRecord);
        log.info(messService.postSuccessLogMess(medicalRecord));
    }

    private void throwIfPersonIsUnknown(MedicalRecord medicalRecord) {
        Optional<Person> optionalPerson = personRepository.selectPersonByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalPerson.orElseThrow(() -> {
                    log.error(messService.postErrorPersonNotFoundLogMess(medicalRecord));
                    return new ApiResourceException(messService.postErrorPersonNotFoundLogMess(medicalRecord));
                }
        );
    }

    private void throwIfMedicalRecordIsPresent(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.ifPresent((mr) -> {
            log.error(messService.postErrorMedicalRecordExistsLog(medicalRecord));
            throw new ApiResourceException(messService.postErrorMedicalRecordExistsLog(medicalRecord));
        });
    }

    public void update(MedicalRecord medicalRecord) {
        throwIfMedicalRecordIsNotFound(medicalRecord, messService.putErrorNoMedRecordLog(medicalRecord));
        medicalRecordRepository.update(medicalRecord);
        log.info(messService.putSuccessLog(medicalRecord));
    }

    private void throwIfMedicalRecordIsNotFound(MedicalRecord medicalRecord, String logMessage) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.orElseThrow(() -> {
            log.error(logMessage);
            return new ApiResourceException(logMessage);
        });
    }

    public void delete(MedicalRecord medicalRecord) {
        throwIfMedicalRecordIsNotFound(medicalRecord, messService.deleteErrorNoMedRecordLogMess(medicalRecord));
        medicalRecordRepository.delete(medicalRecord);
        log.info(messService.deleteSuccessLogMess(medicalRecord));
    }
}
