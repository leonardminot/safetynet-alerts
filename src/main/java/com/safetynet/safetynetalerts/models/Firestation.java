package com.safetynet.safetynetalerts.models;

import jakarta.validation.constraints.NotBlank;

public record Firestation(
        @NotBlank String address,
        Long station
) {
}
