package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.services.PersonService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("person")
@Slf4j
@Tag(name = "Person",
        description = "This controller provides endpoints for managing 'Person' entities in the application. Persons are uniquely identified by the combination of 'firstName' and 'lastName'.")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(
            description = "This endpoint accepts a 'Person' entity data as a request body. It attempts to create and save a new 'Person' entity in the application. The 'Person' is uniquely identified by the combination of 'firstName' and 'lastName'. The creation operation **would fail** if a 'Person' with the same 'firstName' and 'lastName' already exists.",
            summary = "Attempts to create and stores a new 'Person' entity in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - A new 'Person' entity has been created successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Conflict - A 'Person' entity with the same 'firstName' and 'lastName' already exists. The create operation has been aborted.",
                            responseCode = "409"
                    )
            }
    )
    @PostMapping
    void createNewPerson(@RequestBody @Valid Person person) {
        log.info("New request : POST /person - Body : " + person);
        personService.createPerson(person);
    }

    @Operation(
            description = "This endpoint accepts a 'Person' entity data as a request body. It attempts to update an existing 'Person' entity in the application. The 'firstName' and 'lastName' in the request should match an existing 'Person' registered in the application.",
            summary = "Attempts to update an existing 'Person' entity in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - The existing 'Person' entity has been updated successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found - No existing 'Person' entity matches the provided 'firstName' and 'lastName'. The update operation has been aborted.",
                            responseCode = "404"
                    )
            }
    )
    @PutMapping
    void updatePerson(@RequestBody @Valid Person person) {
        log.info("New request : PUT /person - Body : " + person);
        personService.updatePerson(person);
    }

    @Operation(
            description = """
                    This endpoint accepts a 'Person' entity data as a request body. It attempts to delete an existing 'Person' entity in the application. The 'firstName' and 'lastName' in the request should match an existing 'Person' registered in the application.
                    
                        If a 'MedicalRecord' entity is associated with the 'Person' that is being deleted, the 'MedicalRecord' will also be deleted.
                    
                    *Note*: A request body with just the 'firstName' and 'lastName' is sufficient for this operation, any additional fields in the request body are not utilized.""",
            summary = "Attempts to delete an existing 'Person' entity in the application database.",
            responses = {
                    @ApiResponse(
                            description = "Successful Operation - The existing 'Person' entity has been deleted successfully.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found - No existing 'Person' entity matches the provided 'firstName' and 'lastName'. The delete operation has been aborted.",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping
    void deletePerson(@RequestBody @Valid Person person) {
        log.info("New request : DELETE /person - Body : " + person);
        personService.delete(person);
    }

    @GetMapping
    @Hidden
    List<Person> getPersons() {
        return personService.persons();
    }
}
