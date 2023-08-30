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

        log.info(String.format("On POST /medicalRecord : success for create %s %s medical record",
                medicalRecord.firstName(),
                medicalRecord.lastName()));
    }

    private void throwIfPersonIsUnknown(MedicalRecord medicalRecord) {
        personRepository.selectPersonByName(medicalRecord.firstName(), medicalRecord.lastName())
                .orElseThrow(() -> {
                            log.error(String.format("On POST /medicalRecord : Impossible to create Medical Record for %s %s : unknown person",
                                    medicalRecord.firstName(),
                                    medicalRecord.lastName()));
                            return new ApiResourceException(
                                    String.format("Impossible to create Medical Record for %s %s : unknown person",
                                            medicalRecord.firstName(),
                                            medicalRecord.lastName())
                            );
                        }
                );
    }

    private void throwIfMedicalRecordIsPresent(MedicalRecord medicalRecord) {
        medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName())
                .ifPresent((mr) -> {
                    log.error(String.format("On POST /medicalRecord : medicalRecord for %s %s already exists",
                            medicalRecord.firstName(),
                            medicalRecord.lastName()));
                    throw new ApiResourceException(String.format("Error while create Medical Record for %s %s : a medical record already exists",
                            medicalRecord.firstName(),
                            medicalRecord.lastName()));
                });
    }

    public void update(MedicalRecord medicalRecord) {
        throwIfCurrentMedicalRecordNotFound(medicalRecord);

        medicalRecordRepository.update(medicalRecord);

        log.info(String.format("On PUT /medicalRecord : success for update %s %s medical record",
                medicalRecord.firstName(),
                medicalRecord.lastName()));

    }

    private void throwIfCurrentMedicalRecordNotFound(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.orElseThrow(
                () -> {
                    log.error(String.format("On PUT /medicalRecord : error for update %s %s medical record : medical record not found",
                            medicalRecord.firstName(),
                            medicalRecord.lastName()));
                    return new ApiResourceException(
                            String.format("Impossible to update, no medical record for %s %s",
                                    medicalRecord.firstName(),
                                    medicalRecord.lastName())
                    );
                });
    }

    public void delete(MedicalRecord medicalRecord) {
        throwIfMedicalRecordIsNotFound(medicalRecord);

        medicalRecordRepository.delete(medicalRecord);

        log.info(String.format("On DELETE /medicalRecord : success for delete %s %s medical record",
                medicalRecord.firstName(),
                medicalRecord.lastName()));
    }

    private void throwIfMedicalRecordIsNotFound(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.orElseThrow(() -> {
            log.error(String.format("On DELETE /medicalRecord : error for delete %s %s medical record : medical record not found",
                    medicalRecord.firstName(),
                    medicalRecord.lastName()));
            return new ApiResourceException(
                    String.format("Impossible to delete Medical Record for %s %s : medical record not found",
                            medicalRecord.firstName(),
                            medicalRecord.lastName())
            );
        });
    }
}
