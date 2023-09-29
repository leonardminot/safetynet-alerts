package com.safetynet.safetynetalerts.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record MedicalRecord(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy") @Schema(description = "Birthdate (in American format: mm/dd/yyyy)",
                example = "01/31/1990") LocalDate birthdate,
        List<String> medications,
        List<String> allergies
) {
}
