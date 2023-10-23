package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.services.ChildAlertService;
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
@RequestMapping("childAlert")
@Tag(name = "Child Alert",
        description = """
    This controller provides an endpoint to get a list of children living at a given address. All household members are also returned.
    """
)
public class ChildAlertController {
    private final ChildAlertService childAlertService;

    @Autowired
    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    @Operation(
            description = """
        This endpoint returns a list of children (any individual 18 years old or less) living at the address given in parameters.\s
        The list includes the first name and last name of each child, their age, and a list of other household members.\s
        If there are no children, this endpoint return an empty string.""",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved child information or returned an empty string if no children are present.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    List<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        return childAlertService.getChildAlertAtAddress(address);
    }
}
