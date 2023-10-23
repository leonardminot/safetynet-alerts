package com.safetynet.safetynetalerts.mockressources.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.models.MedicalRecord;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordsMockedData {
    public static List<MedicalRecord> createMedicalRecordsMockedDataList() {
        List<MedicalRecord> mockedList = new ArrayList<>();

        MedicalRecord magnusRecord = getMagnusRecord();

        MedicalRecord miniMagnusRecord = getMiniMagnusRecord();

        MedicalRecord maximeRecord = getMaximeRecord();

        MedicalRecord gariRecord = getGariRecord();

        MedicalRecord miniMaximeRecord = getMiniMaximeRecord();

        MedicalRecord miniAlirezaRecord = getMiniAlirezaRecord();

        mockedList.add(magnusRecord);
        mockedList.add(maximeRecord);
        mockedList.add(miniMagnusRecord);
        mockedList.add(gariRecord);
        mockedList.add(miniMaximeRecord);
        mockedList.add(miniAlirezaRecord);

        return mockedList;
    }

    public static MedicalRecord getMiniAlirezaRecord() {
        return new MedicalRecord(
                "mini-Alireza",
                "mini-Firouzja",
                LocalDate.parse("2023-06-18"),
                List.of("hydrapermazol:100mg"),
                List.of("Aspirin")
        );
    }

    public static MedicalRecord getMiniMaximeRecord() {
        return new MedicalRecord(
                "mini-Maxime",
                "mini-Vachier-Lagrave",
                LocalDate.parse("2020-10-21"),
                List.of(),
                List.of("Shellfish")
        );
    }

    public static MedicalRecord getGariRecord() {
        return new MedicalRecord(
                "Gari",
                "Kasparov",
                LocalDate.parse("1963-04-13"),
                List.of(),
                List.of()
        );
    }

    public static MedicalRecord getMaximeRecord() {
        return new MedicalRecord(
                "Maxime",
                "Vachier-Lagrave",
                LocalDate.parse("1991-10-21"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of()
        );
    }

    public static MedicalRecord getMiniMagnusRecord() {
        return new MedicalRecord(
                "miniMagnus",
                "miniCarlsen",
                LocalDate.parse("2020-11-30"),
                List.of(),
                List.of()
        );
    }

    public static MedicalRecord getMagnusRecord() {
        return new MedicalRecord(
                "Magnus",
                "Carlsen",
                LocalDate.parse("1990-11-30"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );
    }

    public static List<MedicalRecord> createMedicalRecordsMockedDataListWithAllEntries() {
        MedicalRecord alirezaRecord = getAlirezaRecord();

        List<MedicalRecord> mockedList = createMedicalRecordsMockedDataList();
        mockedList.add(alirezaRecord);

        return mockedList;
    }

    public static MedicalRecord getAlirezaRecord() {
        return new MedicalRecord(
                "Alireza",
                "Firouzja",
                LocalDate.parse("2003-06-18"),
                List.of(),
                List.of()
        );
    }

    public static void createMedicalRecordsMockedData(String filePath) throws IOException {
        ObjectMapper objectMapper = MyAppConfig.objectMapper();
        List<MedicalRecord> mockedList = createMedicalRecordsMockedDataList();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedList);
    }

    public static void createMedicalRecordsMockedDataWithAllEntries(String filePath) throws IOException {
        ObjectMapper objectMapper = MyAppConfig.objectMapper();
        List<MedicalRecord> mockedList = createMedicalRecordsMockedDataListWithAllEntries();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedList);
    }
}
