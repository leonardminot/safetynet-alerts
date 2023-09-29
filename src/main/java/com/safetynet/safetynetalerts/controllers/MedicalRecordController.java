package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.services.MedicalRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("medicalRecord")
@Slf4j
@Tag(name = "2. Medical Records Controller")
public class MedicalRecordController {

    private MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public void createMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        log.info("New request : POST /medicalRecord - Body : " + medicalRecord);
        medicalRecordService.createRecord(medicalRecord);
    }

    @PutMapping
    public void updateMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        log.info("New request : PUT /medicalRecord - Body : " + medicalRecord);
        medicalRecordService.update(medicalRecord);
    }

    @DeleteMapping
    public void deleteMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        log.info("New request : DELETE /medicalRecord - Body : " + medicalRecord);
        medicalRecordService.delete(medicalRecord);
    }
}
