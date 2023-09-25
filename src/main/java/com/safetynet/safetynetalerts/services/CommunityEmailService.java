package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommunityEmailService {

    private final PersonRepository personRepository;

    @Autowired
    public CommunityEmailService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<String> getEmail(String city) {
        List<Person> persons = personRepository.getPersons();
        List<String> responseBody = persons.stream()
                .filter(person -> person.city().equals(city))
                .map(Person::email)
                .distinct()
                .toList();
        log.info(String.format(
                "GET /communityEmail?city=%s - Success: request return with body [%s]",
                city,
                responseBody
        ));
        return responseBody;
    }
}
