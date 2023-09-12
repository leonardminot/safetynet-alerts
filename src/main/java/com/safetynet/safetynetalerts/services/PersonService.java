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
    private final PersonMessageService messageService;

    @Autowired
    public PersonService(PersonRepository personRepository, PersonMessageService messageService) {
        this.personRepository = personRepository;
        this.messageService = messageService;
    }

    public void createPerson(Person person) {
        throwIfPersonExists(person);
        personRepository.savePerson(person);
        log.info(messageService.postSuccessLogMess(person));
    }

    private void throwIfPersonExists(Person person) {
        Optional<Person> personInDB = personRepository.selectPersonByName(person.firstName(), person.lastName());
        personInDB.ifPresent(p -> {
            log.error(messageService.postErrorPersonExistsLogMess(person));
            throw new ApiResourceException(messageService.postErrorPersonExistsLogMess(person));
        });
    }

    public void updatePerson(Person person) {
        throwIfPersonNotFound(person, messageService.putErrorPersonExistsLogMess(person));
        personRepository.update(person);
        log.info(messageService.putSuccessLogMess(person));
    }

    private void throwIfPersonNotFound(Person person, String logMessage) {
        Optional<Person> personInDB = personRepository.selectPersonByName(person.firstName(), person.lastName());
        personInDB.orElseThrow(() -> {
            log.error(logMessage);
            return new ApiResourceException(logMessage);
        });
    }

    public List<Person> persons() {
        return personRepository.getPersons();
    }

    public void delete(Person personToDelete) {
        // TODO : lorsqu'on supprime une personne : on doit supprimer le Medical Record associé
        throwIfPersonNotFound(personToDelete, messageService.deleteErrorNoPersonFoundLogMess(personToDelete));
        personRepository.delete(personToDelete);
        log.info(messageService.deleteSuccessLogMess(personToDelete));
    }
}
