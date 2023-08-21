package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class PersonRepositoryTest {

    private PersonRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonRepository();
    }

    @Test
    void itShouldReturnThreePersons() {
        // Given
        List<Person> personList;
        String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons.json";

        // When
        personList = underTest.getPersons(pathToFile);

        // Then
        assertThat(personList).hasSize(3);
    }

    @Test
    void itShouldReturnEmptyListWhenNoData() {
        // Given
        List<Person> personList;
        String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons_empty.json";

        // When
        personList = underTest.getPersons(pathToFile);

        // Then
        assertThat(personList).hasSize(0);

    }
}