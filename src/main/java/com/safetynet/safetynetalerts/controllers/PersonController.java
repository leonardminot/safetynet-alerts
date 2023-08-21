package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    void createNewPerson(@RequestBody @Valid Person person) {
        personService.createPerson(person);
    }
}
