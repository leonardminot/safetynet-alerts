package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.dto.LoadInitialDataDTO;
import com.safetynet.safetynetalerts.exception.ApiRepositoryException;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.InitialLoadDataRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
public class InitialLoadDataServiceTest {
    private InitialLoadDataService initialLoadDataService;

    @Mock
    private InitialLoadDataRepository initialLoadDataRepository;
    @Mock
    private FirestationRepository firestationRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private PersonRepository personRepository;
    @Captor
    private ArgumentCaptor<List<Person>> personArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<MedicalRecord>> medicalRecordArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<Firestation>> firestationArgumentCaptor;

    @BeforeEach
    void setUp() {
        initialLoadDataService = new InitialLoadDataService(
                initialLoadDataRepository,
                firestationRepository,
                medicalRecordRepository,
                personRepository);
    }

    @Test
    void itShouldLoadDataFromDataset() {
        // Given
        List<Person> expectedPersons = List.of(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com")
        );

        List<MedicalRecord> expectedMedicalRecords = List.of(
                new MedicalRecord(
                        "John",
                        "Boyd",
                        LocalDate.of(1984, 3, 6),
                        List.of("aznol:350mg", "hydrapermazol:100mg"),
                        List.of("nillacilan")
                ),
                new MedicalRecord(
                        "Jacob",
                        "Boyd",
                        LocalDate.of(1989, 3, 6),
                        List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"),
                        List.of()
                )
        );

        List<Firestation> expectedFireStations = List.of(
                new Firestation("1509 Culver St", "3"),
                new Firestation("29 15th St", "2"),
                new Firestation("834 Binoc Ave", "3")
        );

        LoadInitialDataDTO mockDataSet = new LoadInitialDataDTO(
                expectedPersons,
                expectedFireStations,
                expectedMedicalRecords
        );

        when(initialLoadDataRepository.loadData()).thenReturn(mockDataSet);

        // When
        initialLoadDataService.loadData();

        // Then
        assertThat(initialLoadDataService.getPersons()).isEqualTo(expectedPersons);
        assertThat(initialLoadDataService.getMedicalRecords()).isEqualTo(expectedMedicalRecords);
        assertThat(initialLoadDataService.getFirestations()).isEqualTo(expectedFireStations);
    }

    @Test
    void itShouldSavePersonsToRepository() {
        // Given
        List<Person> personsToSave = List.of(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com")
        );

        // When
        initialLoadDataService.savePersonsToRepository(personsToSave);

        // Then
        then(personRepository).should().saveInitialData(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(personsToSave);
    }

    @Test
    void itShouldSaveMedicalRecordsToRepository() {
        // Given
        List<MedicalRecord> medicalRecordsToSave = List.of(
                new MedicalRecord(
                        "John",
                        "Boyd",
                        LocalDate.of(1984, 3, 6),
                        List.of("aznol:350mg", "hydrapermazol:100mg"),
                        List.of("nillacilan")
                ),
                new MedicalRecord(
                        "Jacob",
                        "Boyd",
                        LocalDate.of(1989, 3, 6),
                        List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"),
                        List.of()
                )
        );

        // When
        initialLoadDataService.saveMedicalRecordsToRepository(medicalRecordsToSave);

        // Then
        then(medicalRecordRepository).should().saveInitialData(medicalRecordArgumentCaptor.capture());
        assertThat(medicalRecordArgumentCaptor.getValue()).isEqualTo(medicalRecordsToSave);
    }

    @Test
    void itShouldSaveFireStationsToRepository() {
        // Given
        List<Firestation> expectedFireStations = List.of(
                new Firestation("1509 Culver St", "3"),
                new Firestation("29 15th St", "2"),
                new Firestation("834 Binoc Ave", "3")
        );

        // When
        initialLoadDataService.saveFireStationsToRepository(expectedFireStations);

        // Then
        then(firestationRepository).should().saveInitialData(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue()).isEqualTo(expectedFireStations);
    }

    @Test
    void itShouldThrowWhenDataSetNotFound() {
        // Given
        initialLoadDataService = new InitialLoadDataService(
                initialLoadDataRepository,
                firestationRepository,
                medicalRecordRepository,
                personRepository);

        when(initialLoadDataRepository.loadData()).thenThrow(new ApiRepositoryException("Server ERROR - impossible to find initial dataset"));

        // When
        // Then
        assertThatThrownBy(() -> initialLoadDataService.loadData())
                .isInstanceOf(ApiRepositoryException.class)
                .hasMessageContaining("Server ERROR - impossible to find initial dataset");

    }
}
