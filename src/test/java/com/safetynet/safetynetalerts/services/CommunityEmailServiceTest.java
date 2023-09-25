package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
public class CommunityEmailServiceTest {

    private CommunityEmailService communityEmailService;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        communityEmailService = new CommunityEmailService(personRepository);
    }

    @Test
    void itShouldReturnAListOfEmail() {
        // Given
        String city = "Paris";
        List<String> expectedResult = List.of(
                PersonsMockedData.getMaxime().email(),
                PersonsMockedData.getAlireza().email());

        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());

        // When
        List<String> actualResult = communityEmailService.getEmail(city);

        // Then
        assertThat(actualResult).containsExactlyInAnyOrderElementsOf(expectedResult);

    }
}
