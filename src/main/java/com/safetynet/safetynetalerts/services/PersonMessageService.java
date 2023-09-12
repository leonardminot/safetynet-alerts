package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonMessageService {
    public PersonMessageService() {
    }

    String postSuccessLogMess(Person person) {
        return String.format("POST /person - Payload: [%s] - Success: Person [%s %s] successfully registered",
                person,
                person.firstName(),
                person.lastName());
    }

    String postErrorPersonExistsLogMess(Person person) {
        return String.format("POST /person - Payload: [%s] - Error: Person with name [%s %s] already exists",
                person,
                person.firstName(),
                person.lastName());
    }

    String putSuccessLogMess(Person person) {
        return String.format("PUT /person - Payload: [%s] - Success: Person [%s %s] successfully updated",
                person,
                person.firstName(),
                person.lastName());
    }

    String putErrorPersonExistsLogMess(Person person) {
        return String.format("PUT /person - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                person,
                person.firstName(),
                person.lastName());
    }

    String deleteSuccessLogMess(Person personToDelete) {
        return String.format("DELETE /person - Payload: [%s] - Success: Person [%s %s] successfully deleted",
                personToDelete,
                personToDelete.firstName(),
                personToDelete.lastName());
    }

    String deleteErrorNoPersonFoundLogMess(Person personToDelete) {
        return String.format("DELETE /person - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                personToDelete,
                personToDelete.firstName(),
                personToDelete.lastName());
    }

    String deleteMedicalRecordIsNotPresent(Person personToDelete) {
        return String.format("DELETE /person - Payload: [%s] - No medical record associate to [%s %s], nothing to delete",
                personToDelete,
                personToDelete.firstName(),
                personToDelete.lastName());
    }

    String deleteMedicalRecordIsPresent(Person personToDelete) {
        return String.format("DELETE /person - Payload: [%s] - Medical record associate to [%s %s] successfully deleted",
                personToDelete,
                personToDelete.firstName(),
                personToDelete.lastName());
    }
}