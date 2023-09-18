package com.safetynet.safetynetalerts.mockressources.utils;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.PersonEmergencyInformationDTO;

import java.util.List;

public class FireAlertMockedData {
    public static FireAlertDTO getMockedData() {
        // Mocked data for address "1990 Rue de la Tour"

        PersonEmergencyInformationDTO maxime = new PersonEmergencyInformationDTO(
                PersonsMockedData.getMaxime().firstName(),
                PersonsMockedData.getMaxime().lastName(),
                PersonsMockedData.getMaxime().phone(),
                32,
                MedicalRecordsMockedData.getMaximeRecord().medications(),
                MedicalRecordsMockedData.getMaximeRecord().allergies()
        );

        PersonEmergencyInformationDTO alireza = new PersonEmergencyInformationDTO(
                PersonsMockedData.getAlireza().firstName(),
                PersonsMockedData.getAlireza().lastName(),
                PersonsMockedData.getAlireza().phone(),
                20,
                MedicalRecordsMockedData.getAlirezaRecord().medications(),
                MedicalRecordsMockedData.getAlirezaRecord().allergies()
        );

        PersonEmergencyInformationDTO miniMaxime = new PersonEmergencyInformationDTO(
                PersonsMockedData.getMiniMaxime().firstName(),
                PersonsMockedData.getMiniMaxime().lastName(),
                PersonsMockedData.getMiniMaxime().phone(),
                3,
                MedicalRecordsMockedData.getMiniMaximeRecord().medications(),
                MedicalRecordsMockedData.getMiniMaximeRecord().allergies()
        );

        PersonEmergencyInformationDTO miniAlireza = new PersonEmergencyInformationDTO(
                PersonsMockedData.getMiniAlireza().firstName(),
                PersonsMockedData.getMiniAlireza().lastName(),
                PersonsMockedData.getMiniAlireza().phone(),
                0,
                MedicalRecordsMockedData.getMiniAlirezaRecord().medications(),
                MedicalRecordsMockedData.getMiniAlirezaRecord().allergies()
        );

        List<PersonEmergencyInformationDTO> fireAlertPersonDTOList = List.of(miniMaxime, maxime, alireza, miniAlireza);

        return new FireAlertDTO("2", fireAlertPersonDTOList);

    }
}
