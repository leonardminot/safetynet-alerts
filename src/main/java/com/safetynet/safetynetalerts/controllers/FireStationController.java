package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.services.FireStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("firestation")
@Slf4j
@Tag(name = "FireStation",
        description = """
        This controller provides endpoints for managing 'FireStation' entities in the application. A 'Firestation' entity is a mapping between an address, which should be unique, and a station number.
        
        *Note*: A single station number can cover multiple addresses.
        """
)
public class FireStationController {

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @Operation(
            description = """
                This endpoint accepts a 'FireStation' entity data as a request body and attempts to create and save a new mapping between an address and a FireStation number.
                The operation **will fail** if there already exists a 'FireStation' with the same mapping, or if the provided address is already associated with a different firestation number.""",
            summary = "Attempts to create and store a new 'FireStation' mapping in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - A new 'FireStation' mapping has been created successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Conflict - A 'FireStation' mapping with the same attributes already exists, or the given address is already associated with a different firestation number. The operation has been aborted.",
                            responseCode = "409"
                    )
            }
    )
    @PostMapping
    void createNewMapping(@RequestBody @Valid Firestation firestation) {
        fireStationService.createMapping(firestation);
    }

    @Operation(
            description = """
            This endpoint accepts a 'FireStation' entity data as a request body and attempts to update the FireStation number associated to the address.
            The operation **will fail** if the address is unknown.""",
            summary = "Attempts to update the 'FireStation' number associated with the address in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - The mapping between the address and the firestation has been updated successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Conflict - The address is unknown. The operation has been aborted.",
                            responseCode = "409"
                    )
            }
    )
    @PutMapping
    void updateMapping(@RequestBody @Valid Firestation firestation) {
        fireStationService.updateMapping(firestation);
    }

    @Operation(
            description = """
        This endpoint accepts a 'FireStation' entity data as a request body and attempts to delete the mapping.
        The operation **will fail** if the address is unknown.

        *Note*: Only the address is required, the other fields are not used in this operation.""",
            summary = "Attempts to delete the 'FireStation' number associated with the address in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - The mapping between the address and the firestation has been deleted successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Conflict - The address is unknown. The operation has been aborted.",
                            responseCode = "409"
                    )
            }
    )
    @DeleteMapping
    void deleteMapping(@RequestBody @Valid Firestation firestation) {
        fireStationService.deleteMapping(firestation);
    }

    @Operation(
            description = """
        This endpoint accepts a station number as a parameter and attempts to delete all mappings associated with this station number.
        The operation **fails** if the station number is unknown.""",
            summary = "Attempts to delete all mappings associated with a specific station number in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - All mappings associated with the station number have been deleted successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Conflict - The station number is not associated with any mappings. The operation has been aborted.",
                            responseCode = "409"
                    )
            }
    )
    @DeleteMapping("/{stationNumber}")
    void deleteStationNumber(@PathVariable String stationNumber) {
        fireStationService.deleteStation(stationNumber);
    }
}
