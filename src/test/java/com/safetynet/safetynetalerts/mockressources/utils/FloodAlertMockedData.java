package com.safetynet.safetynetalerts.mockressources.utils;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.dto.PersonEmergencyInformationDTO;
import com.safetynet.safetynetalerts.dto.EmergencyInfoForAddressDTO;
import com.safetynet.safetynetalerts.models.MedicalRecord;

import java.util.List;

import static com.safetynet.safetynetalerts.utils.AgeCalculation.getAge;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getAllergies;
import static com.safetynet.safetynetalerts.utils.GetMedicalHistory.getMedications;

public class FloodAlertMockedData {
    public static FloodAlertDTO getFloodAlertMockedDataForStation1() {
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

        EmergencyInfoForAddressDTO rueDuFou = new EmergencyInfoForAddressDTO(
                "105 Rue du Fou",
                List.of(gari)
        );

        EmergencyInfoForAddressDTO rueDeLaDame = new EmergencyInfoForAddressDTO(
                "007 Rue de la Dame",
                List.of(magnus, miniMagnus)
        );

        return new FloodAlertDTO(
                "1",
                List.of(rueDeLaDame, rueDuFou)
        );

    }

    public static FloodAlertDTO getFloodAlertMockedDataForStation2() {
        // Simulation of station 1
        // - station 2: 1 address:
        //       - "1990 Rue de la Tour"
        //          - mini-Alireza
        //          - mini-Maxime
        //          - Alireza
        //          - Maxime


        List<MedicalRecord> mockedMedicalRecords = MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries();

        PersonEmergencyInformationDTO miniAlireza = getEmergencyMiniAlireza(mockedMedicalRecords);

        PersonEmergencyInformationDTO miniMaxime = getEmergencyMiniMaxime(mockedMedicalRecords);

        PersonEmergencyInformationDTO alireza = getEmergencyAlireza(mockedMedicalRecords);

        PersonEmergencyInformationDTO maxime = getEmergencyMaxime(mockedMedicalRecords);

        EmergencyInfoForAddressDTO rueDeLaTour = new EmergencyInfoForAddressDTO(
                "1990 Rue de la Tour",
                List.of(maxime, alireza, miniMaxime, miniAlireza)
        );


        return new FloodAlertDTO(
                "2",
                List.of(rueDeLaTour)
        );

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

    public static PersonEmergencyInformationDTO getEmergencyMiniAlireza(List<MedicalRecord> mockedMedicalRecords) {
        return new PersonEmergencyInformationDTO(
                PersonsMockedData.getMiniAlireza().firstName(),
                PersonsMockedData.getMiniAlireza().lastName(),
                PersonsMockedData.getMiniAlireza().phone(),
                getAge(PersonsMockedData.getMiniAlireza(), mockedMedicalRecords),
                getMedications(PersonsMockedData.getMiniAlireza(), mockedMedicalRecords),
                getAllergies(PersonsMockedData.getMiniAlireza(), mockedMedicalRecords)
        );
    }

    public static PersonEmergencyInformationDTO getEmergencyMiniMaxime(List<MedicalRecord> mockedMedicalRecords) {
        return new PersonEmergencyInformationDTO(
                PersonsMockedData.getMiniMaxime().firstName(),
                PersonsMockedData.getMiniMaxime().lastName(),
                PersonsMockedData.getMiniMaxime().phone(),
                getAge(PersonsMockedData.getMiniMaxime(), mockedMedicalRecords),
                getMedications(PersonsMockedData.getMiniMaxime(), mockedMedicalRecords),
                getAllergies(PersonsMockedData.getMiniMaxime(), mockedMedicalRecords)
        );
    }

    public static PersonEmergencyInformationDTO getEmergencyAlireza(List<MedicalRecord> mockedMedicalRecords) {
        return new PersonEmergencyInformationDTO(
                PersonsMockedData.getAlireza().firstName(),
                PersonsMockedData.getAlireza().lastName(),
                PersonsMockedData.getAlireza().phone(),
                getAge(PersonsMockedData.getAlireza(), mockedMedicalRecords),
                getMedications(PersonsMockedData.getAlireza(), mockedMedicalRecords),
                getAllergies(PersonsMockedData.getAlireza(), mockedMedicalRecords)
        );
    }

    public static PersonEmergencyInformationDTO getEmergencyMaxime(List<MedicalRecord> mockedMedicalRecords) {
        return new PersonEmergencyInformationDTO(
                PersonsMockedData.getMaxime().firstName(),
                PersonsMockedData.getMaxime().lastName(),
                PersonsMockedData.getMaxime().phone(),
                getAge(PersonsMockedData.getMaxime(), mockedMedicalRecords),
                getMedications(PersonsMockedData.getMaxime(), mockedMedicalRecords),
                getAllergies(PersonsMockedData.getMaxime(), mockedMedicalRecords)
        );
    }
}
