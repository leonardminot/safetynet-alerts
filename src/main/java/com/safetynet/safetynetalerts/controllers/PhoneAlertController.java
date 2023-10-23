package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.services.PhoneAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("phoneAlert")
@Tag(name = "Phone Alert",
        description = """
           This controller provides an endpoint to get a list of phone numbers of the residents serviced by the fire station.
           """)
public class PhoneAlertController {
    private final PhoneAlertService phoneAlertService;

    @Autowired
    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    @Operation(
            description = """
            This endpoint returns a list of phone numbers of the residents serviced by the fire station, which is given in the parameter.""",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved phone numbers or returned an empty list if the fire station services no residents.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    public List<String> getPhoneAlert(@RequestParam("firestation") String firestation) {
        return phoneAlertService.getPhoneNumbersForFireStation(firestation);
    }
}
