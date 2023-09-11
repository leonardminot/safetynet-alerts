package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void createPerson(Person person) {
        Optional<Person> personInDB = personRepository.selectPersonByName(person.firstName(), person.lastName());

        personInDB.ifPresentOrElse(p -> {
                    log.error(String.format("On POST /person : person %s %s already exists", person.firstName(), person.lastName()));
                    throw new ApiResourceException(
                            String.format("person %s %s already exists", person.firstName(), person.lastName()));
                },
                () -> {
                    log.info(String.format("On POST /person : Success for the creation of [%s %s] :" + person, person.firstName(), person.lastName()));
                    personRepository.savePerson(person);
                });
    }

    public void updatePerson(Person person) {
        Optional<Person> personInDB = personRepository.selectPersonByName(person.firstName(), person.lastName());

        personInDB.ifPresentOrElse(p -> {
                    log.info(String.format("On PUT /person : Success for the update of [%s %s] :" + person, person.firstName(), person.lastName()));
                    personRepository.update(person);
                },
                () -> {
                    log.error(String.format("On PUT /person : person %s %s doesn't exist", person.firstName(), person.lastName()));
                    throw new ApiResourceException(
                            String.format("person %s %s doesn't exist", person.firstName(), person.lastName()));
                });
    }

    public List<Person> persons() {
        return personRepository.getPersons();
    }

    public void delete(Person personToDelete) {
        // TODO : lorsqu'on supprime une personne : doit-on également supprimer les MedicalRecords associés ?
        Optional<Person> personInDB = personRepository.selectPersonByName(personToDelete.firstName(), personToDelete.lastName());

        personInDB.ifPresentOrElse(p -> {
                    log.info(String.format("On PUT /person : Success for the suppression of [%s %s] :" + personToDelete, personToDelete.firstName(), personToDelete.lastName()));
                    personRepository.delete(personToDelete);
                },
                () -> {
                    log.error(String.format("On DELETE /person : person %s %s doesn't exist", personToDelete.firstName(), personToDelete.lastName()));
                    throw new ApiResourceException(
                            String.format("person %s %s doesn't exist", personToDelete.firstName(), personToDelete.lastName()));
                });
    }
}
