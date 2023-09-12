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
                "1990 Rue de la Tour",
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


        mockedList.add(magnus);
        mockedList.add(maxime);
        mockedList.add(alireza);
        mockedList.add(gari);

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

        MedicalRecord maximeRecord = new MedicalRecord(
                "Maxime",
                "Vachier-Lagrave",
                LocalDate.parse("1990-10-21"),
                List.of(),
                List.of()
        );

        mockedList.add(magnusRecord);
        mockedList.add(maximeRecord);

        return mockedList;
    }

    public static void createMedicalRecordsMockedData(String filePath) throws IOException {
        ObjectMapper objectMapper = MyAppConfig.objectMapper();
        List<MedicalRecord> mockedList = createMedicalRecordsMockedDataList();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedList);
    }

    public static void clearJsonFile(String filePath) throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

}
