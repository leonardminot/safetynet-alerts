package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiCreateResourceException;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

    @BeforeEach
    void setUp() {
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

    @Test
    void itShouldThrowWhenPersonAlreadyExists() {
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

        given(personRepository.selectPersonByName(person.firstName(), person.lastName())).willReturn(Optional.of(person));

        // When
        // Then
        assertThatThrownBy(() -> personService.createPerson(person))
                .isInstanceOf(ApiCreateResourceException.class)
                .hasMessageContaining(
                        String.format("person %s %s already exists", person.firstName(), person.lastName()));
        then(personRepository).should(never()).savePerson(any(Person.class));
    }

    @Test
    void itShouldUpdateAPerson() {
        // Given
        // ... A person in the DB with new values for email and phone number
        Person maximeToUpdate = new Person(
                "Maxime",
                "Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "111-222-3333",
                "maxime.vachierlagrave@email.com"
        );

        // When
        personService.updatePerson(maximeToUpdate);

        // Then
        then(personRepository).should().update(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(maximeToUpdate);
    }
}