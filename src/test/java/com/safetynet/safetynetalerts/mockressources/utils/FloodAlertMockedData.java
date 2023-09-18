package com.safetynet.safetynetalerts.mockressources.utils;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.dto.PersonEmergencyInformationDTO;
import com.safetynet.safetynetalerts.dto.PersonsAtAddressDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.AgeCalculation.getAge;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getAllergies;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getMedications;

public class FloodAlertMockedData {
    public static List<FloodAlertDTO> getFloodAlertMockedDataForStation1() {
        // Simulation of station 1
        // - station 1: 2 addresses:
        //       - "105 Rue du Fou"
        //          - Gari
        //       - "007 Rue de la Dame"
        //          - miniMagnus
        //          - Magnus

        List<MedicalRecord> mockedMedicalRecords = MedicalRecordsMockedData.createMedicalRecordsMockedDataList();

        PersonEmergencyInformationDTO gari = getEmergencyGari(mockedMedicalRecords);

        PersonEmergencyInformationDTO miniMagnus = getEmergencyMiniMagnus(mockedMedicalRecords);

        PersonEmergencyInformationDTO magnus = getEmergencyMagnus(mockedMedicalRecords);

        PersonsAtAddressDTO rueDuFou = new PersonsAtAddressDTO(
                "105 Rue du Fou",
                List.of(gari)
        );

        PersonsAtAddressDTO rueDeLaDame = new PersonsAtAddressDTO(
                "007 Rue de la Dame",
                List.of(miniMagnus, magnus)
        );

        FloodAlertDTO floodAlertDTO = new FloodAlertDTO(
                "1",
                List.of(rueDuFou, rueDeLaDame)
        );

        return List.of(floodAlertDTO);

    }

    public static PersonEmergencyInformationDTO getEmergencyMagnus(List<MedicalRecord> mockedMedicalRecords) {
        return new PersonEmergencyInformationDTO(
                PersonsMockedData.getMagnus().firstName(),
                PersonsMockedData.getMagnus().lastName(),
                PersonsMockedData.getMagnus().phone(),
                getAge(PersonsMockedData.getMagnus(), mockedMedicalRecords),
                getMedications(PersonsMockedData.getMagnus(), mockedMedicalRecords),
                getAllergies(PersonsMockedData.getMagnus(), mockedMedicalRecords)
        );
    }

    public static PersonEmergencyInformationDTO getEmergencyMiniMagnus(List<MedicalRecord> mockedMedicalRecords) {
        return new PersonEmergencyInformationDTO(
                PersonsMockedData.getMiniMagnus().firstName(),
                PersonsMockedData.getMiniMagnus().lastName(),
                PersonsMockedData.getMiniMagnus().phone(),
                getAge(PersonsMockedData.getMiniMagnus(), mockedMedicalRecords),
                getMedications(PersonsMockedData.getMiniMagnus(), mockedMedicalRecords),
                getAllergies(PersonsMockedData.getMiniMagnus(), mockedMedicalRecords)
        );
    }

    public static PersonEmergencyInformationDTO getEmergencyGari(List<MedicalRecord> mockedMedicalRecords) {
        return new PersonEmergencyInformationDTO(
                PersonsMockedData.getGari().firstName(),
                PersonsMockedData.getGari().lastName(),
                PersonsMockedData.getGari().phone(),
                getAge(PersonsMockedData.getGari(), mockedMedicalRecords),
                getMedications(PersonsMockedData.getGari(), mockedMedicalRecords),
                getAllergies(PersonsMockedData.getGari(), mockedMedicalRecords)
        );
    }
}
