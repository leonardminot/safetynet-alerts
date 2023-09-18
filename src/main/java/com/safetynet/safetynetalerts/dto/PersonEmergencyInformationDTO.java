package com.safetynet.safetynetalerts.dto;

import java.util.List;

public record PersonEmergencyInformationDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        long age,
        List<String> medications,
        List<String> allergies
) {
}
