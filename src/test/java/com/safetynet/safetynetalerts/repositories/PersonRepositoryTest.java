package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class PersonRepositoryTest {

    private PersonRepository underTestWithData;

    private PersonRepository underTestWithoutData;


    @BeforeEach
    void setUp() {
        underTestWithData = new PersonRepository(
                "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons.json",
                new ObjectMapper());

        underTestWithoutData = new PersonRepository(
                "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons_empty.json",
                new ObjectMapper());
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
}