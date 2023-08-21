package com.safetynet.safetynetalerts.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Person(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String address,
        String city,
        String zip,
        String phone,
        @Email String email
) {
}
