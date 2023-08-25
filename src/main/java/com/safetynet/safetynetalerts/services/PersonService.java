package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiCreateResourceException;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void createPerson(Person person) {
        Optional<Person> personInDB = personRepository.selectPersonByName(person.firstName(), person.lastName());

        personInDB.ifPresentOrElse(p -> {
                    throw new ApiCreateResourceException(
                            String.format("person %s %s already exists", person.firstName(), person.lastName()));
                },
                () -> personRepository.savePerson(person));
    }
}
