package com.safetynet.safetynetalerts.dto;

import java.util.List;

public record FloodAlertDTO(
        String stationNumber,
        List<EmergencyInfoForAddressDTO> personsGroupByAddress
) {
}
