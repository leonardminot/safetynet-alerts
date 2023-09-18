package com.safetynet.safetynetalerts.mockressources.utils;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertPersonDTO;

import java.util.List;

public class FireAlertMockedData {
    public static FireAlertDTO getMockedData() {
        // Mocked data for address "1990 Rue de la Tour"

        FireAlertPersonDTO maxime = new FireAlertPersonDTO(
                PersonsMockedData.getMaxime().firstName(),
                PersonsMockedData.getMaxime().lastName(),
                PersonsMockedData.getMaxime().phone(),
                32,
                MedicalRecordsMockedData.getMaximeRecord().medications(),
                MedicalRecordsMockedData.getMaximeRecord().allergies()
        );

        FireAlertPersonDTO alireza = new FireAlertPersonDTO(
                PersonsMockedData.getAlireza().firstName(),
                PersonsMockedData.getAlireza().lastName(),
                PersonsMockedData.getAlireza().phone(),
                20,
                MedicalRecordsMockedData.getAlirezaRecord().medications(),
                MedicalRecordsMockedData.getAlirezaRecord().allergies()
        );

        FireAlertPersonDTO miniMaxime = new FireAlertPersonDTO(
                PersonsMockedData.getMiniMaxime().firstName(),
                PersonsMockedData.getMiniMaxime().lastName(),
                PersonsMockedData.getMiniMaxime().phone(),
                3,
                MedicalRecordsMockedData.getMiniMaximeRecord().medications(),
                MedicalRecordsMockedData.getMiniMaximeRecord().allergies()
        );

        FireAlertPersonDTO miniAlireza = new FireAlertPersonDTO(
                PersonsMockedData.getMiniAlireza().firstName(),
                PersonsMockedData.getMiniAlireza().lastName(),
                PersonsMockedData.getMiniAlireza().phone(),
                0,
                MedicalRecordsMockedData.getMiniAlirezaRecord().medications(),
                MedicalRecordsMockedData.getMiniAlirezaRecord().allergies()
        );

        List<FireAlertPersonDTO> fireAlertPersonDTOList = List.of(miniMaxime, maxime, alireza, miniAlireza);

        return new FireAlertDTO("2", fireAlertPersonDTOList);

    }
}
