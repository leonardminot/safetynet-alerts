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
        personRepository.selectPersonByName(medicalRecord.firstName(), medicalRecord.lastName())
                .ifPresentOrElse(
                        person -> medicalRecordRepository.saveRecord(medicalRecord),
                        () -> {
                            throw new ApiResourceException(
                                    String.format("Impossible to create Medical Record for %s %s : unknown person",
                                            medicalRecord.firstName(),
                                            medicalRecord.lastName())
                            );
                        }
                );

    }

    public void update(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.selectMedicalRecordByName(medicalRecord.firstName(), medicalRecord.lastName());
        optionalMedicalRecord.ifPresentOrElse(mr -> {
                    log.info(String.format("On PUT /medicalRecord : success for update %s %s medical record",
                            mr.firstName(),
                            mr.lastName()));
                    medicalRecordRepository.update(medicalRecord);
                },
                () -> {
                    log.info(String.format("On PUT /medicalRecord : error for update %s %s medical record : medical record not found",
                            medicalRecord.firstName(),
                            medicalRecord.lastName()));
                    throw new ApiResourceException(
                            String.format("Impossible to update, no medical record for %s %s",
                                    medicalRecord.firstName(),
                                    medicalRecord.lastName())
                    );
                });
    }
}
