package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.FirestationRepository;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
public class InitialLoadDataServiceTest {
    private InitialLoadDataService initialLoadDataService;
    @Mock
    private FirestationRepository firestationRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        initialLoadDataService = new InitialLoadDataService(
                "src/test/java/com/safetynet/safetynetalerts/mockressources/mockdataset.json",
                MyAppConfig.objectMapper(),
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

        // When
        initialLoadDataService.loadData();

        // Then
        assertThat(initialLoadDataService.getPersons()).isEqualTo(expectedPersons);
        assertThat(initialLoadDataService.getMedicalRecords()).isEqualTo(expectedMedicalRecords);
        assertThat(initialLoadDataService.getFirestations()).isEqualTo(expectedFireStations);
    }
}
