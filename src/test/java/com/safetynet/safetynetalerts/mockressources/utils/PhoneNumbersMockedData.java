package com.safetynet.safetynetalerts.mockressources.utils;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumbersMockedData {
    public static List<String> getPhoneNumbersForStation1() {
        // Station 1 cover "007 Rue de la Dame" and "105 Rue du Fou",
        String magnusNumber = PersonsMockedData.getMagnus().phone();
        String gariNumber = PersonsMockedData.getGari().phone();

        List<String> expectedNumbers = new ArrayList<>();
        expectedNumbers.add(gariNumber);
        expectedNumbers.add(magnusNumber);

        return expectedNumbers;
    }
}
