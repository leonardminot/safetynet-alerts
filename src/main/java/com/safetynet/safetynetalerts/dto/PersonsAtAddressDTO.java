package com.safetynet.safetynetalerts.dto;

import java.util.List;

public record PersonsAtAddressDTO(
        String address,
        List<PersonEmergencyInformationDTO> personsEmergencyInformation
) {
}
