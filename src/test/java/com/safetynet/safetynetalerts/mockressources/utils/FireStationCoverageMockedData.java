package com.safetynet.safetynetalerts.mockressources.utils;

import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;

public class FireStationCoverageMockedData {
    public static PersonsCoveredByFirestationDTO getMagnus() {
        return new PersonsCoveredByFirestationDTO(
                PersonsMockedData.getMagnus().firstName(),
                PersonsMockedData.getMagnus().lastName(),
                PersonsMockedData.getMagnus().address(),
                PersonsMockedData.getMagnus().phone()
        );
    }

    public static PersonsCoveredByFirestationDTO getMiniMagnus() {
        return new PersonsCoveredByFirestationDTO(
                PersonsMockedData.getMiniMagnus().firstName(),
                PersonsMockedData.getMiniMagnus().lastName(),
                PersonsMockedData.getMiniMagnus().address(),
                PersonsMockedData.getMiniMagnus().phone()
        );
    }

    public static PersonsCoveredByFirestationDTO getGari() {
        return new PersonsCoveredByFirestationDTO(
                PersonsMockedData.getGari().firstName(),
                PersonsMockedData.getGari().lastName(),
                PersonsMockedData.getGari().address(),
                PersonsMockedData.getGari().phone()
        );
    }
}
