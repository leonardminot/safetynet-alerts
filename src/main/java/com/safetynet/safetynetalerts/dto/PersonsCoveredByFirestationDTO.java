package com.safetynet.safetynetalerts.dto;

public record PersonsCoveredByFirestationDTO(
        String firstName,
        String lastName,
        String address,
        String phone
) {
}
