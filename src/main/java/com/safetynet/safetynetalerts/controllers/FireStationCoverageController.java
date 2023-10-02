package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.FirestationStationNumberDTO;
import com.safetynet.safetynetalerts.dto.PersonsCoveredByFirestationDTO;
import com.safetynet.safetynetalerts.services.FireStationCoverageService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("firestation")
@Slf4j
@Tag(name = "Fire Station Coverage",
        description = """
     This controller provides an endpoint to retrieve a list of individuals covered by a fire station, as specified by a stationNumber parameter. The coverage list includes the first name, last name, address, and phone number of each individual, and counts of adults and children in the serviced area.
     """
)
public class FireStationCoverageController {

    private final FireStationCoverageService fireStationCoverageService;

    @Autowired
    public FireStationCoverageController(FireStationCoverageService fireStationCoverageService) {
        this.fireStationCoverageService = fireStationCoverageService;
    }

    @Operation(
            description = """
        This endpoint provides, based on a given stationNumber parameter, a list of individuals covered by the corresponding fire station. The list includes specific information such as first name, last name, address, and phone number. It also provides a count of the number of adults and the number of children (any individual 18 years old or less) in the serviced area.""",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved individuals' information covered by the fire station along with a count of adults and children.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    public FirestationStationNumberDTO getPersonCoverage(@RequestParam String stationNumber) {
        log.info("New Request : GET /firestation?stationNumber=" + stationNumber);
        List<PersonsCoveredByFirestationDTO> personsCoveredByFirestationDTOS = fireStationCoverageService.findPersonsCoveredByFirestation(stationNumber);
        long totalOfAdults = fireStationCoverageService.getTotalAdults(stationNumber);
        long totalOfChildren = fireStationCoverageService.getTotalChildren(stationNumber);

        FirestationStationNumberDTO responseBody = new FirestationStationNumberDTO(
                totalOfAdults,
                totalOfChildren,
                personsCoveredByFirestationDTOS
        );

        log.info(String.format("GET /firestation?stationNumber=%s - Success: request return with body %s",
                stationNumber,
                responseBody));
        return responseBody;
    }
}
