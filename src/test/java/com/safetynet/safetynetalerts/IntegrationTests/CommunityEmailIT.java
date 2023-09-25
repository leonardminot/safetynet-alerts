package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.databind.JavaType;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class CommunityEmailIT {

    @Autowired
    private MockMvc mockMvc;

    private final String filePathMockPersons;

    public CommunityEmailIT(@Value("${safetynetalerts.jsonpath.persons}") String filePathMockPersons) {
        this.filePathMockPersons = filePathMockPersons;
    }

    @BeforeEach
    void setUp() throws IOException {
        PersonsMockedData.createPersonMockedData(filePathMockPersons);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockPersons);
    }

    @Test
    void itShouldReturnAListOfPhoneNumber() throws Exception {
        // Given
        String city = "Paris";
        List<String> expectedResult = List.of(
                PersonsMockedData.getMaxime().email(),
                PersonsMockedData.getAlireza().email());

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail?city=" + city)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JavaType type = MyAppConfig.objectMapper().getTypeFactory().constructCollectionType(List.class, String.class);
        List<String> actualResult = MyAppConfig.objectMapper().readValue(jsonResponse, type);

        assertThat(actualResult).containsExactlyInAnyOrderElementsOf(expectedResult);
    }
}
