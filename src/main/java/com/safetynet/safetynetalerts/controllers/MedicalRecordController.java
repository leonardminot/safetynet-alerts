package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.services.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("medicalRecord")
@Slf4j
public class MedicalRecordController {

    private MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public void createMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        medicalRecordService.createRecord(medicalRecord);
    }
}
