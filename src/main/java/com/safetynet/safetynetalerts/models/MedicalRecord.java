package com.safetynet.safetynetalerts.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record MedicalRecord(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy") LocalDate date,
        List<String> medications,
        List<String> allergies

) {
}
