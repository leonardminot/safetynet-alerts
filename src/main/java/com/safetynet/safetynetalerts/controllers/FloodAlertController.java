package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.services.FloodAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("flood")
@Slf4j
@Tag(name = "Flood Alert",
        description = """
This controller provides an endpoint for retrieving information about households served by the specified fire stations. The fire station numbers are provided as a list in the request parameters. The response from this request organizes people by address and includes the name, phone number, and age of the inhabitants, along with their medical history (medication, dosage, and allergies).
""")
public class FloodAlertController {
    private final FloodAlertService floodAlertService;

    @Autowired
    public FloodAlertController(FloodAlertService floodAlertService) {
        this.floodAlertService = floodAlertService;
    }

    @Operation(
            description = """
        Based on the list of fire station numbers provided as request parameters, this endpoint provides information about all households served by these fire stations. The resulting data organizes people by address and includes the name, phone number, age of inhabitants, as well as their medical history (medication, dosage, and allergies).
        """,
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved information about all households served by the specified fire stations, along with residents' details and their medical history.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("stations")
    List<FloodAlertDTO> getFloodAlert(@RequestParam List<String> stations) {
        log.info("New request: GET /flood/stations?stations=" + stations);
        return floodAlertService.getFloodAlert(stations);
    }
}
