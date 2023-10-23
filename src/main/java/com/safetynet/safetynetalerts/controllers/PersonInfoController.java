package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.services.PersonInfoService;
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
@RequestMapping("personInfo")
@Tag(name = "Person Information",
        description = """
        This controller provides an endpoint for retrieving the name, address, age, email, and medical history (medication, dosage, allergies) of a resident. The resident is identified by their firstName and lastName, which are mandatory request parameters.
        """)
public class PersonInfoController {

    private final PersonInfoService personInfoService;

    @Autowired
    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @Operation(
            description = """
This endpoint, based on the mandatory firstName and lastName parameters, provides the name, address, age, email, and medical history (medication, dosage, allergies) of a resident.
""",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved the details of the resident.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    private List<PersonInfoDTO> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        return personInfoService.getPersonInfo(firstName, lastName);
    }
}
