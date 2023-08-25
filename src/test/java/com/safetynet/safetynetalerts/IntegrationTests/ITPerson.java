package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ITPerson {

    @Autowired
    private MockMvc mockMvc;

    private final String filePathMockPersons;

    private final PersonRepository personRepository;

    @Autowired
    public ITPerson(@Value("${safetynetalerts.jsonpath.persons}") String filePathMockPersons, PersonRepository personRepository) {
        this.filePathMockPersons = filePathMockPersons;
        this.personRepository = personRepository;
    }

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createPersonMockedData(filePathMockPersons);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockPersons);
    }

    @Test
    void itShouldCreateANewPerson() throws Exception {
        // Given
        Person newPerson = new Person(
                "Harry",
                "Potter",
                "Gryfondor Poudlard",
                "Pré-au-lard",
                "777777",
                "123-456-7890",
                "harry.potter@poudlard.fr"
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(personToJson(newPerson))));

        // Then
        List<Person> persons = personRepository.getPersons();
        resultActions.andExpect(status().isOk());
        assertThat(persons).hasSize(4);
        assertThat(persons.get(persons.size() - 1)).isEqualTo(newPerson);
    }

    @Test
    void itShouldNotOKWhenCreateNewPersonThatAlreadyExist() throws Exception {
        //TODO : mettre en place la gestion des exceptions personnalisés

        // Given a person already in DB
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
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(personToJson(magnus))));

        // Then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().is4xxClientError());
        assertThat(contentAsString).contains("person Magnus Carlsen already exists");
        List<Person> persons = personRepository.getPersons();
        assertThat(persons).hasSize(3);

    }

    private String personToJson(Person person) {
        try {
            return new ObjectMapper().writeValueAsString(person);
        } catch (JsonProcessingException e) {
            fail("");
            return null;
        }
    }
}
