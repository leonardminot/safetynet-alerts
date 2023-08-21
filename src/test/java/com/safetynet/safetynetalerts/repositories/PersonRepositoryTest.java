package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.mockressources.utils.CreateMockedData;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class PersonRepositoryTest {

    private PersonRepository underTestWithData;

    private PersonRepository underTestWithoutData;


    @BeforeEach
    void setUp() throws IOException {
        CreateMockedData.createPersonMockedData();

        underTestWithData = new PersonRepository(
                "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons.json",
                new ObjectMapper());

        underTestWithoutData = new PersonRepository(
                "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons_empty.json",
                new ObjectMapper());
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        CreateMockedData.clearJsonFile(
                "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons.json");
    }

    @Test
    void itShouldReturnThreePersons() {
        // Given
        List<Person> personList;

        // When
        personList = underTestWithData.getPersons();

        // Then
        assertThat(personList).hasSize(3);
    }

    @Test
    void itShouldReturnEmptyListWhenNoData() {
        // Given
        List<Person> personList;

        // When
        personList = underTestWithoutData.getPersons();

        // Then
        assertThat(personList).hasSize(0);

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
        optionalPersonThatExists = underTestWithData.selectCustomerByName("Magnus", "Carlsen");
        optionalPersonThatDidntExist = underTestWithData.selectCustomerByName("Wesley", "So");

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
        underTestWithData.savePerson(personToSave);

        // Then
        List<Person> allPerson = underTestWithData.getPersons();
        assertThat(allPerson).hasSize(4);
        assertThat(allPerson.get(allPerson.size() - 1)).isEqualTo(personToSave);

    }
}