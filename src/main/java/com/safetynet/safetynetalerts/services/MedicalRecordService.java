package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiNotFoundException;
import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
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
        throwIfMedicalRecordIsPresent(medicalRecord);
        throwIfBirthdateInTheFuture(medicalRecord);
        throwIfPersonIsUnknown(medicalRecord);
        medicalRecordRepository.saveRecord(medicalRecord);
    }

    private void throwIfPersonIsUnknown(MedicalRecord medicalRecord) {
        Optional<Person> optionalPerson = personRepository.selectPersonByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalPerson.orElseThrow(() -> new ApiNotFoundException(messService.postErrorPersonNotFoundLogMess(medicalRecord))
        );
    }

    private void throwIfBirthdateInTheFuture(MedicalRecord medicalRecord) {
        if (medicalRecord.birthdate().isAfter(LocalDate.now())) {
            throw new ApiResourceException(messService.postErrorBirthdateInFutureLogMess(medicalRecord));
        }

    }

    private void throwIfMedicalRecordIsPresent(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.ifPresent((mr) -> {
            throw new ApiResourceException(messService.postErrorMedicalRecordExistsLog(medicalRecord));
        });
    }

    public void update(MedicalRecord medicalRecord) {
        throwIfMedicalRecordIsNotFound(medicalRecord, messService.putErrorNoMedRecordLog(medicalRecord));
        medicalRecordRepository.update(medicalRecord);
    }

    private void throwIfMedicalRecordIsNotFound(MedicalRecord medicalRecord, String logMessage) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.orElseThrow(() -> new ApiNotFoundException(logMessage));
    }

    public void delete(MedicalRecord medicalRecord) {
        throwIfMedicalRecordIsNotFound(medicalRecord, messService.deleteErrorNoMedRecordLogMess(medicalRecord));
        medicalRecordRepository.delete(medicalRecord);
    }
}
