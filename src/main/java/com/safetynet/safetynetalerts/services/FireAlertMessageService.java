package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;

public class FireAlertMessageService {
    public FireAlertMessageService() {
    }

    String getSuccessFireAlertLogMess(String address, FireAlertDTO fireAlertDTO) {
        return String.format("GET /fire?address=%s - Success: request return with body: [%s]",
                address,
                fireAlertDTO);
    }
}