package com.safetynet.safetynetalerts.dto;

import java.util.List;

public record FireAlertDTO(
        String FireStationNumber,
        List<FireAlertPersonDTO> personsAtAddress
) {
}
