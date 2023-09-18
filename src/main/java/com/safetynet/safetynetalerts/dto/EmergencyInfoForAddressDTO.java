package com.safetynet.safetynetalerts.dto;

import java.util.List;

public record EmergencyInfoForAddressDTO(
        String address,
        List<PersonEmergencyInformationDTO> personsEmergencyInformation
) {
}
