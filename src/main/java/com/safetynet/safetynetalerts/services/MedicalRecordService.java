package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
}
