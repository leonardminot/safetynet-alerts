package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.mockressources.utils.FireStationMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PhoneNumbersMockedData;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PhoneAlertServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepository personRepository;

    private PhoneAlertService phoneAlertService;

    @BeforeEach
    void setUp() {
        this.phoneAlertService = new PhoneAlertService(personRepository, firestationRepository);
    }

    @Test
    void itShouldReturn2PhoneNumber() {
        // Given
        List<String> expectedPhoneNumbers = PhoneNumbersMockedData.getPhoneNumbersForStation1();
        String stationNumber = "1";

        when(firestationRepository.getFirestations()).thenReturn(FireStationMockedData.createFirestationsMockedDataList());
        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());

        // When
        List<String> actualPhoneNumbers = phoneAlertService.getPhoneNumbersForFireStation(stationNumber);

        // Then
        assertThat(actualPhoneNumbers).containsExactlyInAnyOrderElementsOf(expectedPhoneNumbers);

    }

    @Test
    void itShouldRemovePhoneNumberThatAreDuplicates() {
        // Given
        List<String> expectedPhoneNumbers = PhoneNumbersMockedData.getPhoneNumbersForStation1();
        String stationNumber = "1";

        when(firestationRepository.getFirestations()).thenReturn(FireStationMockedData.createFirestationsMockedDataList());
        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataListForPhoneNumberValidation());

        // When
        List<String> actualPhoneNumbers = phoneAlertService.getPhoneNumbersForFireStation(stationNumber);

        // Then
        assertThat(actualPhoneNumbers).containsExactlyInAnyOrderElementsOf(expectedPhoneNumbers);

    }
}
