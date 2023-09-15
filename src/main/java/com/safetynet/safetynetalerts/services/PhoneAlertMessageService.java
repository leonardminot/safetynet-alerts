package com.safetynet.safetynetalerts.services;

import java.util.List;

public class PhoneAlertMessageService {
    public PhoneAlertMessageService() {
    }

    String getSuccessPhoneAlertLogMess(String stationNumber, List<String> phoneNumbersForFireStation) {
        return String.format("GET /phoneAlert?firestation=%s - Success: request return with body [%s]",
                stationNumber,
                phoneNumbersForFireStation);
    }
}