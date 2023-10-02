package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiNotFoundException;
import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
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

    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, PersonMessageService messageService, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.messageService = messageService;
        this.medicalRecordRepository = medicalRecordRepository;
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
            return new ApiNotFoundException(logMessage);
        });
    }

    public List<Person> persons() {
        return personRepository.getPersons();
    }

    public void delete(Person personToDelete) {
        throwIfPersonNotFound(personToDelete, messageService.deleteErrorNoPersonFoundLogMess(personToDelete));
        personRepository.delete(personToDelete);
        log.info(messageService.deleteSuccessLogMess(personToDelete));
        deleteAssociatedMedicalRecordIfPresent(personToDelete);
    }

    private void deleteAssociatedMedicalRecordIfPresent(Person personToDelete) {
        medicalRecordRepository.selectMedicalRecordByName(personToDelete.firstName(), personToDelete.lastName())
                .ifPresentOrElse((mr) -> {
                            medicalRecordRepository.delete(mr);
                            log.info(messageService.deleteMedicalRecordIsPresent(personToDelete));
                        },
                        () -> log.info(messageService.deleteMedicalRecordIsNotPresent(personToDelete)));
    }
}
