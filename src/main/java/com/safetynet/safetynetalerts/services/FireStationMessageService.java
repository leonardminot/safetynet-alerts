package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.Firestation;
import org.springframework.stereotype.Service;

@Service
public class FireStationMessageService {
    public FireStationMessageService() {
    }

    String postErrorFirestationFoundLogMess(Firestation firestation) {
        return String.format("POST /firestation - Payload: [%s] - Error: Firestation number [%s] for address [%s] already exists",
                firestation.toString(),
                firestation.station(),
                firestation.address());
    }

    String postSuccessLogMessage(Firestation firestation) {
        return String.format("POST /firestation - Payload: [%s] - Success: Firestation number [%s] for address [%s] successfully registered",
                firestation,
                firestation.station(),
                firestation.address());
    }

    String putSuccessLogMess(Firestation firestation) {
        return String.format("PUT /firestation - Payload: [%s] - Success: Firestation at address [%s] successfully updated with new station number [%s]",
                firestation.toString(),
                firestation.address(),
                firestation.station());
    }

    String putErrorFirestationNotFoundLogMess(Firestation firestation) {
        return String.format("PUT /firestation - Payload: [%s] - Error: No firestation found at address [%s]",
                firestation,
                firestation.address());
    }

    String deleteErrorFirestationNotFoundLogMess(Firestation firestation) {
        return String.format("DELETE /firestation - Payload: [%s] - Error: No firestation found at address [%s]",
                firestation,
                firestation.address());
    }

    String deleteSuccessLogMess(Firestation firestation) {
        return String.format("DELETE /firestation - Payload: [%s] - Success: Firestation at address [%s] successfully deleted",
                firestation.toString(),
                firestation.address());
    }

    String deleteAllStationsSuccessLogMess(String stationNumber) {
        return String.format("DELETE /firestation - Payload: {\"station\":\"%s\"} - Success: All firestations associated with station number %s successfully deleted",
                stationNumber,
                stationNumber);
    }

    String deleteErrorStationNumberNotFoundLogMess(String stationNumber) {
        return String.format("DELETE /firestation - Payload: {\"station\":\"%s\"} - Error: No firestations found associated with station number %s",
                stationNumber,
                stationNumber);
    }
}