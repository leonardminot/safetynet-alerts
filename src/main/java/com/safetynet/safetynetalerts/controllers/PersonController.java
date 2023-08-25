package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping
    void updatePerson(@RequestBody @Valid Person person) {
        personService.updatePerson(person);
    }

    @GetMapping
    List<Person> getPersons() {
        return personService.persons();
    }
}
