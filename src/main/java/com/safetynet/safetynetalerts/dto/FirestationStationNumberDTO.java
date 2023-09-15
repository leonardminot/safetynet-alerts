package com.safetynet.safetynetalerts.dto;
import java.util.List;

public record FirestationStationNumberDTO(
        long totalAdults,
        long totalChildren,
        List<PersonsCoveredByFirestationDTO> personsCovered
) {
}
