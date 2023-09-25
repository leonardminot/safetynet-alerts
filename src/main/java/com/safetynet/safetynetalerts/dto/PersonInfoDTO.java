package com.safetynet.safetynetalerts.dto;

import java.util.List;

public record PersonInfoDTO(
        String firstName,
        String lastName,
        long age,
        String mail,
        List<String> medications,
        List<String> allergies

) {
}
