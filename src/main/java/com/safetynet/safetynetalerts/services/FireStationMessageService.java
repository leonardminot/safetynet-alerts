package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.Firestation;
import org.springframework.stereotype.Service;

@Service
public class FireStationMessageService {
    public FireStationMessageService() {
    }

    String postErrorFirestationFoundLogMess(Firestation firestation) {
        return String.format("Impossible to create: [%s] - Error: Firestation number [%s] for address [%s] already exists",
                firestation.toString(),
                firestation.station(),
                firestation.address());
    }

    String putErrorFirestationNotFoundLogMess(Firestation firestation) {
        return String.format("Impossible to update: [%s] - Error: No firestation found at address [%s]",
                firestation,
                firestation.address());
    }

    String deleteErrorFirestationNotFoundLogMess(Firestation firestation) {
        return String.format("Impossible to delete: [%s] - Error: No firestation found at address [%s]",
                firestation,
                firestation.address());
    }

    String deleteErrorStationNumberNotFoundLogMess(String stationNumber) {
        return String.format("Impossible to delete: {\"station\":\"%s\"} - Error: No firestations found associated with station number %s",
                stationNumber,
                stationNumber);
    }

    String postErrorAddressHasStationLogMess(Firestation firestation) {
        return String.format("Impossible to create: [%s] - Error: A Firestation for address [%s] already exists",
                firestation,
                firestation.address());
    }
}