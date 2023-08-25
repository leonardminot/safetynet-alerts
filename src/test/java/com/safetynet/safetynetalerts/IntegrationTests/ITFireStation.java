package com.safetynet.safetynetalerts.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
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
import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ITFireStation {

    @Autowired
    private MockMvc mockMvc;

    private final String filePathMockFireStations;

    private final FirestationRepository firestationRepository;

    @Autowired
    public ITFireStation(@Value("${safetynetalerts.jsonpath.firestations}") String filePathMockFireStations, FirestationRepository firestationRepository) {
        this.filePathMockFireStations = filePathMockFireStations;
        this.firestationRepository = firestationRepository;
    }

    @BeforeEach
    void setUp() throws IOException {
        ManageMockedData.createFirestationsMockedData(filePathMockFireStations);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        ManageMockedData.clearJsonFile(filePathMockFireStations);
    }

    @Test
    void itShouldCreateANewMapping() throws Exception {
        // Given
        Firestation firestation = new Firestation(
                "64 rue des case",
                "7"
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(personToJson(firestation))));

        // Then
        List<Firestation> firestations = firestationRepository.getFirestations();
        resultActions.andExpect(status().isOk());
        assertThat(firestations).hasSize(3);
        assertThat(firestations.get(firestations.size() - 1)).isEqualTo(firestation);
    }

    @Test
    void itShouldNotCreateANewMappingWhenMappingAlreadyExists() throws Exception {
        // Given
        Firestation rueDeLaDame = new Firestation(
                "007 Rue de la Dame",
                "1"
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(personToJson(rueDeLaDame))));

        // Then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().is4xxClientError());
        assertThat(contentAsString).contains(
                String.format("mapping address : [%s] with station : [%s] already exist",
                        rueDeLaDame.address(),
                        rueDeLaDame.station()));
        List<Firestation> firestations = firestationRepository.getFirestations();
        assertThat(firestations).hasSize(2);
    }

    @Test
    void itShouldUpdateAMapping() throws Exception {
        // Given
        // ... existant mapping
        Firestation currentMapping = new Firestation(
                "007 Rue de la Dame",
                "1"
        );

        // ... to update to a new mapping
        Firestation futureMapping = new Firestation(
                "007 Rue de la Dame",
                "7"
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(personToJson(futureMapping))));

        // Then
        List<Firestation> firestations = firestationRepository.getFirestations();
        resultActions.andExpect(status().isOk());
        assertThat(firestations).hasSize(2);
        Optional<Firestation> optionalFirestation = firestations.stream()
                .filter(fs -> fs.address().equals(currentMapping.address()))
                .findAny();
        assertThat(optionalFirestation)
                .isPresent()
                .hasValueSatisfying(fs -> assertThat(fs).isEqualTo(futureMapping));
    }

    @Test
    void itShouldNotUpdateWhenUnknownAddress() throws Exception {
        // Given
        Firestation unknownAddress = new Firestation(
                "64 rue des case",
                "7"
        );

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(personToJson(unknownAddress))));

        // Then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().is4xxClientError());
        assertThat(contentAsString).contains(
                String.format("No mapping available for address [%s]", unknownAddress.address()));
        List<Firestation> firestations = firestationRepository.getFirestations();
        assertThat(firestations).hasSize(2);

    }

    private String personToJson(Firestation firestation) {
        try {
            return new ObjectMapper().writeValueAsString(firestation);
        } catch (JsonProcessingException e) {
            fail("");
            return null;
        }
    }
}
