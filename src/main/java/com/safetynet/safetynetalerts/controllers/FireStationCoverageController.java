package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.FirestationStationNumberDTO;
import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.services.FireStationCoverageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("firestation")
@Slf4j
public class FireStationCoverageController {

    private final FireStationCoverageService fireStationCoverageService;

    @Autowired
    public FireStationCoverageController(FireStationCoverageService fireStationCoverageService) {
        this.fireStationCoverageService = fireStationCoverageService;
    }

    @GetMapping
    public FirestationStationNumberDTO getPersonCoverage(@RequestParam String stationNumber) {
        List<PersonsCoveredByFirestationDTO> personsCoveredByFirestationDTOS = fireStationCoverageService.getCoverageForAStationNumber(stationNumber);
        long totalOfAdults = fireStationCoverageService.getTotalAdults(stationNumber);
        long totalOfChildren = fireStationCoverageService.getTotalChildren(stationNumber);

        return new FirestationStationNumberDTO(
                totalOfAdults,
                totalOfChildren,
                personsCoveredByFirestationDTOS
        );
    }
}
