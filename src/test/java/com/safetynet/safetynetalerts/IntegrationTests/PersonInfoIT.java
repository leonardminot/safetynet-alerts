package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.databind.JavaType;
import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.services.InitialLoadDataService;
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
public class PersonInfoIT {
    @Autowired
    private MockMvc mockMvc;

    private final InitialLoadDataService initialLoadDataService;

    @Value("${safetynetalerts.jsonpath.dataset}")
    private String filePath;

    @Autowired
    public PersonInfoIT(InitialLoadDataService initialLoadDataService) {
        this.initialLoadDataService = initialLoadDataService;
    }

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createMockedDataWithAllEntries(filePath);
        initialLoadDataService.initializeData();
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePath);
        initialLoadDataService.clearData();
    }

    @Test
    void itShouldReturnPersonInfo() throws Exception {
        // Given
        PersonInfoDTO magnusInformation = new PersonInfoDTO(
                "Magnus",
                "Carlsen",
                32,
                "magnusd@email.com",
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/personInfo?firstName=Magnus&lastName=Carlsen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JavaType personInfoDTOListType = MyAppConfig.objectMapper().getTypeFactory().constructCollectionType(List.class, PersonInfoDTO.class);
        List<PersonInfoDTO> actualResult = MyAppConfig.objectMapper().readValue(jsonResponse, personInfoDTOListType);

        assertThat(actualResult).containsExactlyInAnyOrderElementsOf(List.of(magnusInformation));
    }
}
