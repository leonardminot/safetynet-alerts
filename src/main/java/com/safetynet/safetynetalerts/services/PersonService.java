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
        throwIfPersonExists(person);
        personRepository.savePerson(person);
        log.info(postSuccessLogMess(person));
    }

    private void throwIfPersonExists(Person person) {
        Optional<Person> personInDB = personRepository.selectPersonByName(person.firstName(), person.lastName());
        personInDB.ifPresent(p -> {
            log.error(postErrorPersonExistsLogMess(person));
            throw new ApiResourceException(postErrorPersonExistsLogMess(person));
        });
    }

    private String postSuccessLogMess(Person person) {
        return String.format("POST /person - Payload: [%s] - Success: Person [%s %s] successfully registered",
                person,
                person.firstName(),
                person.lastName());
    }

    private String postErrorPersonExistsLogMess(Person person) {
        return String.format("POST /person - Payload: [%s] - Error: Person with name [%s %s] already exists",
                person,
                person.firstName(),
                person.lastName());
    }

    public void updatePerson(Person person) {
        throwIfPersonNotFound(person, putErrorPersonExistsLogMess(person));
        personRepository.update(person);
        log.info(putSuccessLogMess(person));
    }

    private void throwIfPersonNotFound(Person person, String logMessage) {
        Optional<Person> personInDB = personRepository.selectPersonByName(person.firstName(), person.lastName());
        personInDB.orElseThrow(() -> {
            log.error(logMessage);
            return new ApiResourceException(logMessage);
        });
    }

    private String putSuccessLogMess(Person person) {
        return String.format("PUT /person - Payload: [%s] - Success: Person [%s %s] successfully updated",
                person,
                person.firstName(),
                person.lastName());
    }

    private String putErrorPersonExistsLogMess(Person person) {
        return String.format("PUT /person - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                person,
                person.firstName(),
                person.lastName());
    }

    public List<Person> persons() {
        return personRepository.getPersons();
    }

    public void delete(Person personToDelete) {
        // TODO : lorsqu'on supprime une personne : on doit supprimer le Medical Record associé
        throwIfPersonNotFound(personToDelete, deleteErrorNoPersonFoundLogMess(personToDelete));
        personRepository.delete(personToDelete);
        log.info(deleteSuccessLogMess(personToDelete));
    }

    private String deleteSuccessLogMess(Person personToDelete) {
        return String.format("DELETE /person - Payload: [%s] - Success: Person [%s %s] successfully deleted",
                personToDelete,
                personToDelete.firstName(),
                personToDelete.lastName());
    }

    private String deleteErrorNoPersonFoundLogMess(Person personToDelete) {
        return String.format("DELETE /person - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                personToDelete,
                personToDelete.firstName(),
                personToDelete.lastName());
    }
}
