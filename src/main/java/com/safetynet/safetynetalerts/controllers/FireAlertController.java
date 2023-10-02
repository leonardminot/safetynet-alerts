package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.services.FireAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fire")
@Slf4j
@Tag(name = "Fire Alert",
        description = """
        This controller provides an endpoint for retrieving a list of residents living at a given address together with the number of the fire station serving it. The list includes the name, phone number, age and medical history (medication, dosage, and allergies) of each person.
        """)
public class FireAlertController {
    private final FireAlertService fireAlertService;

    @Autowired
    public FireAlertController(FireAlertService fireAlertService) {
        this.fireAlertService = fireAlertService;
    }

    @Operation(
            description = """
            This endpoint, based on a given address parameter, provides a list of residents living at the given address and the number of the fire station serving the address. The list includes the name, phone number, age, and medical history (medication, dosage, and allergies) of each person.
            """,
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved information of residents and designating fire station.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    FireAlertDTO getFireAlert(@RequestParam("address") String address) {
        log.info("New request : GET /fire?address=" + address);
        return fireAlertService.getFireAlert(address);
    }
}
