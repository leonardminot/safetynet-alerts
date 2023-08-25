package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.services.PersonService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("person")
@Slf4j
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    void createNewPerson(@RequestBody @Valid Person person) {
        log.info("New request : POST /person with body : " + person);
        personService.createPerson(person);
    }

    @PutMapping
    void updatePerson(@RequestBody @Valid Person person) {
        log.info("New request : PUT /person with body : " + person);
        personService.updatePerson(person);
    }

    @DeleteMapping
    void deletePerson(@RequestBody @Valid Person person) {
        log.info("New request : DELETE /person with body : " + person);
        personService.delete(person);
    }

    @GetMapping
    List<Person> getPersons() {
        return personService.persons();
    }
}
