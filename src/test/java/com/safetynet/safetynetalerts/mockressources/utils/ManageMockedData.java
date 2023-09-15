package com.safetynet.safetynetalerts.mockressources.utils;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManageMockedData {

    public static List<Person> createPersonMockedDataList() {
        List<Person> mockedList = new ArrayList<>();

        Person magnus = new Person(
                "Magnus",
                "Carlsen",
                "007 Rue de la Dame",
                "Oslo",
                "63429",
                "123-456-7890",
                "magnusd@email.com"
        );

        Person maxime = new Person(
                "Maxime",
                "Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "987-654-3210",
                "maxime@email.com"
        );

        Person alireza = new Person(
                "Alireza",
                "Firouzja",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "000-111-2222",
                "alireza@email.com"
        );

        Person gari = new Person(
                "Gari",
                "Kasparov",
                "105 Rue du Fou",
                "Oslo",
                "63429",
                "741-852-9630",
                "gari@email.com"
        );

        Person miniMagnus = new Person(
                "miniMagnus",
                "miniCarlsen",
                "007 Rue de la Dame",
                "Oslo",
                "63429",
                null,
                null
        );

        Person miniMaxime = new Person(
                "mini-Maxime",
                "mini-Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "987-654-3210",
                "maxime@email.com"
        );

        Person miniAlireza = new Person(
                "mini-Alireza",
                "mini-Firouzja",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "000-111-2222",
                "alireza@email.com"
        );


        mockedList.add(magnus);
        mockedList.add(maxime);
        mockedList.add(alireza);
        mockedList.add(gari);
        mockedList.add(miniMagnus);
        mockedList.add(miniMaxime);
        mockedList.add(miniAlireza);

        return mockedList;
    }

    public static void createPersonMockedData(String filePath) throws IOException {

        ObjectMapper objectMapper = MyAppConfig.objectMapper();
        List<Person> mockedList = createPersonMockedDataList();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedList);
    }

    public static List<Firestation> createFirestationsMockedDataList() {
        List<Firestation> mockedList = new ArrayList<>();

        Firestation rueDeLaDame = new Firestation(
                "007 Rue de la Dame",
                "1"
        );

        Firestation rueDuFou= new Firestation(
                "105 Rue du Fou",
                "1"
        );

        Firestation rueDeLaTour = new Firestation(
                "1990 Rue de la Tour",
                "2"
        );

        mockedList.add(rueDeLaDame);
        mockedList.add(rueDeLaTour);
        mockedList.add(rueDuFou);

        return mockedList;
    }

    public static void createFirestationsMockedData(String filePath) throws IOException {
        ObjectMapper objectMapper = MyAppConfig.objectMapper();
        List<Firestation> mockedList = createFirestationsMockedDataList();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedList);
    }

    public static List<MedicalRecord> createMedicalRecordsMockedDataList() {
        List<MedicalRecord> mockedList = new ArrayList<>();

        MedicalRecord magnusRecord = new MedicalRecord(
                "Magnus",
                "Carlsen",
                LocalDate.parse("1990-11-30"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );

        MedicalRecord miniMagnusRecord = new MedicalRecord(
                "miniMagnus",
                "miniCarlsen",
                LocalDate.parse("2020-11-30"),
                List.of(),
                List.of()
        );

        MedicalRecord maximeRecord = new MedicalRecord(
                "Maxime",
                "Vachier-Lagrave",
                LocalDate.parse("1990-10-21"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of()
        );

        MedicalRecord gariRecord = new MedicalRecord(
                "Gari",
                "Kasparov",
                LocalDate.parse("1963-04-13"),
                List.of(),
                List.of()
        );

        MedicalRecord miniMaximeRecord = new MedicalRecord(
                "mini-Maxime",
                "mini-Vachier-Lagrave",
                LocalDate.parse("2019-10-21"),
                List.of(),
                List.of("Shellfish")
        );

        MedicalRecord miniAlirezaRecord = new MedicalRecord(
                "mini-Alireza",
                "mini-Firouzja",
                LocalDate.parse("2023-06-18"),
                List.of("hydrapermazol:100mg"),
                List.of("Aspirin")
        );

        mockedList.add(magnusRecord);
        mockedList.add(maximeRecord);
        mockedList.add(miniMagnusRecord);
        mockedList.add(gariRecord);
        mockedList.add(miniMaximeRecord);
        mockedList.add(miniAlirezaRecord);

        return mockedList;
    }

    public static List<MedicalRecord> createMedicalRecordsMockedDataListWithAllEntries() {
        MedicalRecord alirezaRecord = new MedicalRecord(
                "Alireza",
                "Firouzja",
                LocalDate.parse("2003-06-18"),
                List.of(),
                List.of()
        );

        List<MedicalRecord> mockedList = createMedicalRecordsMockedDataList();
        mockedList.add(alirezaRecord);

        return mockedList;
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

    public static void clearJsonFile(String filePath) throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

}
