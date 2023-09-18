package com.safetynet.safetynetalerts.utils;

import com.safetynet.safetynetalerts.models.Firestation;

import java.util.List;

public class AddressesResearch {
    public static List<String> getCoveredAddressesByFireStationNumber(List<Firestation> firestations, String stationNumber) {
        return firestations.stream()
                .filter(fs -> fs.station().equals(stationNumber))
                .map(Firestation::address)
                .toList();
    }
}
