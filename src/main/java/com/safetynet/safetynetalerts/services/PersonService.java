package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
                    throw new ApiResourceException(
                            String.format("person %s %s already exists", person.firstName(), person.lastName()));
                },
                () -> personRepository.savePerson(person));
    }

    public void updatePerson(Person person) {
        Optional<Person> personInDB = personRepository.selectPersonByName(person.firstName(), person.lastName());

        personInDB.ifPresentOrElse(p -> personRepository.update(person),
                () -> {throw new ApiResourceException(
                        String.format("person %s %s doesn't exist", person.firstName(), person.lastName()));});
    }

    public List<Person> persons() {
        return personRepository.getPersons();
    }
}
