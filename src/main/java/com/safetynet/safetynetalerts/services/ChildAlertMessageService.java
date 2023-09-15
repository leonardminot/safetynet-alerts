package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;

import java.util.List;

public class ChildAlertMessageService {
    public ChildAlertMessageService() {
    }

    String getSuccessChildAlertLogMess(String alertAddress, List<ChildAlertDTO> listOfChildrenAtAddress) {
        return String.format("GET /childAlert?address=%s - Success: request return with body [%s]",
                alertAddress,
                listOfChildrenAtAddress);
    }
}