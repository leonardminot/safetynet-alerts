package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.services.FireStationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("firestation")
@Slf4j
@Tag(name = "3. Fire Station Controller")
public class FireStationController {

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @PostMapping
    void createNewMapping(@RequestBody @Valid Firestation firestation) {
        log.info("New request : POST /firestation - Body : " + firestation);
        fireStationService.createMapping(firestation);
    }

    @PutMapping
    void updateMapping(@RequestBody @Valid Firestation firestation) {
        log.info("New request : PUT /firestation - Body : " + firestation);
        fireStationService.updateMapping(firestation);
    }

    @DeleteMapping
    void deleteMapping(@RequestBody @Valid Firestation firestation) {
        log.info("New request : DELETE /firestation - Body : " + firestation);
        fireStationService.deleteMapping(firestation);
    }

    @DeleteMapping("/{stationNumber}")
    void deleteStationNumber(@PathVariable String stationNumber) {
        log.info("New request : DELETE /firestation - All station number (request param): " + stationNumber);
        fireStationService.deleteStation(stationNumber);
    }
}
