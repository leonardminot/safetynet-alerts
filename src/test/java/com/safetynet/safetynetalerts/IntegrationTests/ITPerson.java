package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ITPerson {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreateANewCustomer() throws Exception {
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
        resultActions.andExpect(status().isOk());
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
