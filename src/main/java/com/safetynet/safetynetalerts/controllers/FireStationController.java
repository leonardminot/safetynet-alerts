package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.services.FireStationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("firestation")
@Slf4j
public class FireStationController {

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @PostMapping
    void createNewMapping(@RequestBody @Valid Firestation firestation) {
        log.info("New request : POST /firestation with body : " + firestation);
        fireStationService.createMapping(firestation);
    }

    @PutMapping
    void updateMapping(@RequestBody @Valid Firestation firestation) {
        log.info("New request : PUT /firestation with body : " + firestation);
        fireStationService.updateMapping(firestation);
    }

    @DeleteMapping
    void deleteMapping(@RequestBody @Valid Firestation firestation) {
        log.info("New request : DELETE /firestation with body : " + firestation);
        fireStationService.deleteMapping(firestation);
    }

    // TODO : vérifier si le endpoint suivant serait valable dans le projet ?
    @DeleteMapping("/stationNumber")
    void deleteStationNumber(@RequestParam String stationNumber) {
        log.info("New request : DELETE /firestation all station with number : " + stationNumber);
        fireStationService.deleteStation(stationNumber);
    }
}
