package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.services.CommunityEmailService;
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
@RequestMapping("communityEmail")
@Tag(name = "Community Email",
        description = """
This controller provides endpoints for retrieving the email addresses of all the residents of a specific city, given the city name as a request parameter.
""")
public class CommunityEmailController {

    private final CommunityEmailService communityEmailService;

    @Autowired
    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }
    @Operation(
            description = """
        This endpoint, based on a given city parameter, provides the email addresses of all the residents of the specified city.
        """,
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved email addresses of all the residents of the specified city.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    List<String> getCommunityEmail(@RequestParam String city) {
        return communityEmailService.getEmail(city);
    }
}
