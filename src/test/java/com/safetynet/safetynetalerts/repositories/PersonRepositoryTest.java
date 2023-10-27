package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class PersonRepositoryTest {

    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepository();
        personRepository.saveInitialData(PersonsMockedData.createPersonMockedDataList());
    }


    @Test
    void itShouldReturnSevenPersons() {
        // Given
        List<Person> personList;

        // When
        personList = personRepository.getPersons();

        // Then
        assertThat(personList).hasSize(7);
    }

    @Test
    void itShouldReturnAPersonWhenAskName() {
        // Given
        Optional<Person> optionalPersonThatExists;
        Optional<Person> optionalPersonThatDidntExist;

        Person magnus = new Person(
                "Magnus",
                "Carlsen",
                "007 Rue de la Dame",
                "Oslo",
                "63429",
                "123-456-7890",
                "magnusd@email.com"
        );

        // When
        optionalPersonThatExists = personRepository.selectPersonByName("Magnus", "Carlsen");
        optionalPersonThatDidntExist = personRepository.selectPersonByName("Wesley", "So");

        // Then
        assertThat(optionalPersonThatExists).isPresent();
        assertThat(optionalPersonThatExists.get()).isEqualTo(magnus);

        assertThat(optionalPersonThatDidntExist).isNotPresent();
    }

    @Test
    void itShouldSaveANewPerson() {
        // Given
        Person personToSave = new Person(
                "Wesley",
                "So",
                "123 Rue du Gambit Roi",
                "New-York",
                "98765",
                "333-333-3333",
                "wesley@email.com"
        );

        // When
        personRepository.savePerson(personToSave);

        // Then
        List<Person> allPerson = personRepository.getPersons();
        assertThat(allPerson).hasSize(8);
        assertThat(allPerson.get(allPerson.size() - 1)).isEqualTo(personToSave);

    }

    @Test
    void itShouldUpdateAPerson() {
        // Given
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
        personRepository.update(maximeToUpdate);

        // Then
        List<Person> allPerson = personRepository.getPersons();
        Optional<Person> maximeInDB = allPerson.stream()
                .filter(p -> p.firstName().equals(maximeToUpdate.firstName()) && p.lastName().equals(maximeToUpdate.lastName()))
                .findFirst();

        assertThat(allPerson).hasSize(7);
        assertThat(maximeInDB).isPresent().hasValueSatisfying(person -> assertThat(person).isEqualTo(maximeToUpdate));

    }

    @Test
    void itShouldUpdateAPersonWithSomeNullFields() {
        // Given
        Person maximeToUpdate = new Person(
                "Maxime",
                "Vachier-Lagrave",
                null,
                null,
                "75014",
                "111-222-3333",
                null
        );

        //... The final person to get
        Person finalMaxime = new Person(
                "Maxime",
                "Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75014",
                "111-222-3333",
                "maxime@email.com"
        );

        // When
        personRepository.update(maximeToUpdate);

        // Then
        List<Person> allPerson = personRepository.getPersons();
        Optional<Person> maximeInDB = allPerson.stream()
                .filter(p -> p.firstName().equals(maximeToUpdate.firstName()) && p.lastName().equals(maximeToUpdate.lastName()))
                .findFirst();

        assertThat(allPerson).hasSize(7);
        assertThat(maximeInDB).isPresent().hasValueSatisfying(person -> assertThat(person).isEqualTo(finalMaxime));

    }

    @Test
    void itShouldDeleteAPerson() {
        // Given
        Person personToDelete = new Person(
                "Maxime",
                "Vachier-Lagrave",
                null,
                null,
                null,
                null,
                null
        );

        // When
        personRepository.delete(personToDelete);

        // Then
        List<Person> allPerson = personRepository.getPersons();
        Optional<Person> maximeInDB = allPerson.stream()
                .filter(p -> p.firstName().equals(personToDelete.firstName()) && p.lastName().equals(personToDelete.lastName()))
                .findFirst();
        assertThat(allPerson).hasSize(6);
        assertThat(maximeInDB).isNotPresent();
    }

    @Test
    void itShouldSaveFirestationsInClassStorage() {
        // Given
        //... Clear datas for tests purpose
        personRepository.saveInitialData(new ArrayList<>());

        List<Person> givenPersons = List.of(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com")
        );

        // When
        personRepository.saveInitialData(givenPersons);

        // Then
        assertThat(personRepository.getPersons()).isEqualTo(givenPersons);
    }
}