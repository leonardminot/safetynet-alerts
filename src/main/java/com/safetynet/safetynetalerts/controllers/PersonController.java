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
@Tag(name = "1. Person Controller",
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

    @PutMapping
    void updatePerson(@RequestBody @Valid Person person) {
        log.info("New request : PUT /person - Body : " + person);
        personService.updatePerson(person);
    }

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
