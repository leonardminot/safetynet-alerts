package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class PersonServiceTest {

    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        personService = new PersonService(personRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void itShouldCreateANewPerson() {
        // Given
        Person person = new Person(
                "Harry",
                "Potter",
                "Gryfondor Poudlard",
                "Pré-au-lard",
                "777777",
                "123-456-7890",
                "harry.potter@poudlard.fr"
                );


        // When
        personService.createPerson(person);

        // Then
        then(personRepository).should().savePerson(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(person);

    }
}