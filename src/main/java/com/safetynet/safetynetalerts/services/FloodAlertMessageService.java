package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloodAlertMessageService {
    public FloodAlertMessageService() {
    }

    String getSuccessFloodAlertLogMess(List<String> stationsAlert, List<FloodAlertDTO> responseBody) {
        return String.format("GET /flood/stations?stations=%s - Success: request return with body: [%s]",
                stationsAlert,
                responseBody);
    }
}