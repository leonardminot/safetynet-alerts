package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.services.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("medicalRecord")
@Slf4j
@Tag(name = "Medical Records",
        description = """
    This controller provides endpoints for managing 'MedicalRecords' entities in the application.\s
    Each 'MedicalRecord' entity is uniquely identified by the combination of 'firstName' and 'lastName' and must correspond to an associated 'Person' entity in the system.
    """)
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @Operation(
            description = "This endpoint accepts a 'Medical Record' entity data as a request body. It attempts to create and save a new 'Medical Record' entity in the application. " +
                    "The 'Medical Record' is uniquely identified by the combination of 'firstName' and 'lastName'. " +
                    "The creation operation **would fail** if a 'Medical Record' with the same 'firstName' and 'lastName' already exists." +
                    "Moreover, a 'Person' entity with the same name must exist before the creation of the Medical Record. Finally, the creation of the Medical Record would fail if the birthdate of the person is in the future.",
            summary = "Attempts to create and stores a new 'Medical Record' entity in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - A new 'Medical Record' entity has been created successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Conflict - A 'Medical Record' entity with the same 'firstName' and 'lastName' already exists, or the associated 'Person' entity does not exist, or the birthdate of the person is in the future. The create operation has been aborted.",
                            responseCode = "409"
                    )
            }
    )
    @PostMapping
    public void createMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        log.info("New request : POST /medicalRecord - Body : " + medicalRecord);
        medicalRecordService.createRecord(medicalRecord);
    }

    @Operation(
            description = "This endpoint accepts data for a 'MedicalRecord' entity via a request body. It attempts to update an existing 'MedicalRecord' entity in the application. The 'firstName' and 'lastName' in the request should correspond to an existing 'MedicalRecord' in the application.",
            summary = "Attempts to update an existing 'MedicalRecord' entity in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - The existing 'MedicalRecord' entity has been successfully updated.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Conflict - No existing 'MedicalRecord' matches the provided 'firstName' and 'lastName'. The update operation has been aborted.",
                            responseCode = "409"
                    )
            }
    )
    @PutMapping
    public void updateMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        log.info("New request : PUT /medicalRecord - Body : " + medicalRecord);
        medicalRecordService.update(medicalRecord);
    }

    @Operation(
            description = """
                    This endpoint accepts a 'MedicalRecord' entity data as a request body. It attempts to delete an existing 'MedicalRecord' entity in the application.
                    The 'firstName' and 'lastName' in the request should match an existing 'MedicalRecord' registered in the application.
                    
                    *Note*: A request body with just the 'firstName' and 'lastName' is sufficient for this operation, any additional fields in the request body are not utilized.""",
            summary = "Attempts to delete an existing 'MedicalRecord' entity in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - The existing 'MedicalRecord' entity has been deleted successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Conflict - No existing 'MedicalRecord' entity matches the provided 'firstName' and 'lastName'. The delete operation has been aborted.",
                            responseCode = "409"
                    )
            }
    )
    @DeleteMapping
    public void deleteMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        log.info("New request : DELETE /medicalRecord - Body : " + medicalRecord);
        medicalRecordService.delete(medicalRecord);
    }
}
