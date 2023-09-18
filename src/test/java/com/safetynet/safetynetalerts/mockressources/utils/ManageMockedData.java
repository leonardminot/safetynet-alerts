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

    public static void clearJsonFile(String filePath) throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

}
