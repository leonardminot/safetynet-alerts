package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class PersonRepositoryTest {

    private PersonRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonRepository();
    }

    @Test
    void itShouldReturnThreePersons() throws IOException {
        // Given
        List<Person> personList;
        String pathToFile = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockpersons.json";

        // When
        personList = underTest.getPersons(pathToFile);

        // Then
        assertThat(personList).hasSize(3);
    }
}