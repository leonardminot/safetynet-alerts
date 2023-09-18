package com.safetynet.safetynetalerts.mockressources.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ManageMockedData {

    public static void clearJsonFile(String filePath) throws FileNotFoundException {
        new PrintWriter(filePath).close();
    }

}
