package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.mockressources.utils.FireStationMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
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
    void itShouldReturn3PhoneNumber() {
        // Given
        List<String> expectedPhoneNumbers = new ArrayList<>(List.of("123-456-7890", "741-852-9630"));
        expectedPhoneNumbers.add(null);
        String stationNumber = "1";

        when(firestationRepository.getFirestations()).thenReturn(FireStationMockedData.createFirestationsMockedDataList());
        when(personRepository.getPersons()).thenReturn(PersonsMockedData.createPersonMockedDataList());

        // When
        List<String> actualPhoneNumbers = phoneAlertService.getPhoneNumbersForFireStation(stationNumber);

        // Then
        assertThat(actualPhoneNumbers).isEqualTo(expectedPhoneNumbers);

    }
}
