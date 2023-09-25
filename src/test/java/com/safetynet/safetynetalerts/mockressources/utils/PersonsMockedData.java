package com.safetynet.safetynetalerts.mockressources.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.models.Person;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PersonsMockedData {
    public static List<Person> createPersonMockedDataList() {
        List<Person> mockedList = new ArrayList<>();

        Person magnus = getMagnus();

        Person maxime = getMaxime();

        Person alireza = getAlireza();

        Person gari = getGari();

        Person miniMagnus = getMiniMagnus();

        Person miniMaxime = getMiniMaxime();

        Person miniAlireza = getMiniAlireza();


        mockedList.add(magnus);
        mockedList.add(maxime);
        mockedList.add(alireza);
        mockedList.add(gari);
        mockedList.add(miniMagnus);
        mockedList.add(miniMaxime);
        mockedList.add(miniAlireza);

        return mockedList;
    }

    public static List<Person> createPersonMockedDataListForPhoneNumberValidation() {
        List<Person> mockedList = new ArrayList<>();

        Person magnus = getMagnus();

        Person maxime = getMaxime();

        Person alireza = getAlireza();

        Person gari = getGari();

        Person miniMagnusWithPhone = getMiniMagnusWithPhoneAndMail();

        Person miniMaxime = getMiniMaxime();

        Person miniAlireza = getMiniAlireza();


        mockedList.add(magnus);
        mockedList.add(maxime);
        mockedList.add(alireza);
        mockedList.add(gari);
        mockedList.add(miniMagnusWithPhone);
        mockedList.add(miniMaxime);
        mockedList.add(miniAlireza);

        return mockedList;
    }

    public static Person getGari() {
        return new Person(
                "Gari",
                "Kasparov",
                "105 Rue du Fou",
                "Oslo",
                "63429",
                "741-852-9630",
                "gari@email.com"
        );
    }

    public static Person getMiniAlireza() {
        return new Person(
                "mini-Alireza",
                "mini-Firouzja",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "000-111-2222",
                "alireza@email.com"
        );
    }

    public static Person getMiniMaxime() {
        return new Person(
                "mini-Maxime",
                "mini-Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "987-654-3210",
                "maxime@email.com"
        );
    }

    public static Person getMiniMagnus() {
        return new Person(
                "miniMagnus",
                "miniCarlsen",
                "007 Rue de la Dame",
                "Oslo",
                "63429",
                null,
                null
        );
    }

    public static Person getMiniMagnusWithPhoneAndMail() {
        return new Person(
                "miniMagnus",
                "miniCarlsen",
                "007 Rue de la Dame",
                "Oslo",
                "63429",
                "123-456-7890",
                "magnusd@email.com"
        );
    }

    public static Person getAlireza() {
        return new Person(
                "Alireza",
                "Firouzja",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "000-111-2222",
                "alireza@email.com"
        );
    }

    public static Person getMaxime() {
        return new Person(
                "Maxime",
                "Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "987-654-3210",
                "maxime@email.com"
        );
    }

    public static Person getMagnus() {
        return new Person(
                "Magnus",
                "Carlsen",
                "007 Rue de la Dame",
                "Oslo",
                "63429",
                "123-456-7890",
                "magnusd@email.com"
        );
    }

    public static void createPersonMockedData(String filePath) throws IOException {

        ObjectMapper objectMapper = MyAppConfig.objectMapper();
        List<Person> mockedList = createPersonMockedDataList();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedList);
    }
}
