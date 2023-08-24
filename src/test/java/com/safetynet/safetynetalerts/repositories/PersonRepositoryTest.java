package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class PersonRepositoryTest {

    private PersonRepository personRepository;

    private final String filePathMockPersons = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons.json";


    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createPersonMockedData(filePathMockPersons);

        personRepository = new PersonRepository(
                filePathMockPersons,
                MyAppConfig.objectMapper());
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockPersons);
    }

    @Test
    void itShouldReturnThreePersons() {
        // Given
        List<Person> personList;

        // When
        personList = personRepository.getPersons();

        // Then
        assertThat(personList).hasSize(3);
    }

    @Test
    void itShouldReturnEmptyListWhenNoData() throws FileNotFoundException {
        // Given
        List<Person> personList;
        // ... empty mockpersons.json
        ManageMockedData.clearJsonFile(filePathMockPersons);

        // When
        personList = personRepository.getPersons();

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
        optionalPersonThatExists = personRepository.selectCustomerByName("Magnus", "Carlsen");
        optionalPersonThatDidntExist = personRepository.selectCustomerByName("Wesley", "So");

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
        assertThat(allPerson).hasSize(4);
        assertThat(allPerson.get(allPerson.size() - 1)).isEqualTo(personToSave);

    }
}