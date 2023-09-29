package com.safetynet.safetynetalerts.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Person(
        @NotBlank(message = "First name should not be empty") String firstName,
        @NotBlank(message = "Last name should not be empty") String lastName,
        String address,
        String city,
        String zip,
        String phone,
        @Email String email
) {
}
