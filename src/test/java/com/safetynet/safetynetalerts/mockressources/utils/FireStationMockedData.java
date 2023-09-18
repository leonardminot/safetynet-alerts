package com.safetynet.safetynetalerts.mockressources.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.models.Firestation;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FireStationMockedData {
    public static List<Firestation> createFirestationsMockedDataList() {
        List<Firestation> mockedList = new ArrayList<>();

        Firestation rueDeLaDame = getRueDeLaDame();

        Firestation rueDuFou= getRueDuFou();

        Firestation rueDeLaTour = getRueDeLaTour();

        mockedList.add(rueDeLaDame);
        mockedList.add(rueDeLaTour);
        mockedList.add(rueDuFou);

        return mockedList;
    }

    public static Firestation getRueDeLaTour() {
        return new Firestation(
                "1990 Rue de la Tour",
                "2"
        );
    }

    public static Firestation getRueDuFou() {
        return new Firestation(
                "105 Rue du Fou",
                "1"
        );
    }

    public static Firestation getRueDeLaDame() {
        return new Firestation(
                "007 Rue de la Dame",
                "1"
        );
    }

    public static void createFirestationsMockedData(String filePath) throws IOException {
        ObjectMapper objectMapper = MyAppConfig.objectMapper();
        List<Firestation> mockedList = createFirestationsMockedDataList();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filePath).toFile(), mockedList);
    }
}
