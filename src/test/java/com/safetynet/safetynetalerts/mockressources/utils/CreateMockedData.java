package com.safetynet.safetynetalerts.mockressources.utils;

import com.safetynet.safetynetalerts.models.Person;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateMockedData {
    public static void createPersonMockedData() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> mockedList = new ArrayList<>();
        String filePath = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons.json";

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

        mockedList.add(magnus);
        mockedList.add(maxime);
        mockedList.add(alireza);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedList);
    }

    public static void clearJsonFile(String filePath) throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

}
