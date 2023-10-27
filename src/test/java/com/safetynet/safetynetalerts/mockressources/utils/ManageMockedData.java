package com.safetynet.safetynetalerts.mockressources.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.dto.LoadInitialDataDTO;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;

public class ManageMockedData {

    public static void clearJsonFile(String filePath) throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

    public static void createMockedDataWithAllEntries(String filePath) throws IOException {
        System.out.println("Création des data mockées");
        LoadInitialDataDTO mockedData;
        ObjectMapper objectMapper = MyAppConfig.objectMapper();

        List<Person> mockedPersons = PersonsMockedData.createPersonMockedDataList();
        List<Firestation> mockedFireStations = FireStationMockedData.createFirestationsMockedDataList();
        List<MedicalRecord> mockedMedicalRecords = MedicalRecordsMockedData.createMedicalRecordsMockedDataListWithAllEntries();

        mockedData = new LoadInitialDataDTO(mockedPersons, mockedFireStations, mockedMedicalRecords);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedData);
    }

}
