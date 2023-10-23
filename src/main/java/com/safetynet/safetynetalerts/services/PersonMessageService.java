package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonMessageService {
    public PersonMessageService() {
    }

    String postErrorPersonExistsLogMess(Person person) {
        return String.format("Impossible to create: [%s] - Error: Person with name [%s %s] already exists",
                person,
                person.firstName(),
                person.lastName());
    }

    String putErrorPersonExistsLogMess(Person person) {
        return String.format("Impossible to update: [%s] - Error: Person with name [%s %s] does not exist",
                person,
                person.firstName(),
                person.lastName());
    }

    String deleteErrorNoPersonFoundLogMess(Person personToDelete) {
        return String.format("Impossible to delete: [%s] - Error: Person with name [%s %s] does not exist",
                personToDelete,
                personToDelete.firstName(),
                personToDelete.lastName());
    }
}