package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.configuration.MyAppConfig;
import com.safetynet.safetynetalerts.dto.LoadInitialDataDTO;
import com.safetynet.safetynetalerts.exception.ApiRepositoryException;
import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("UnitTest")
class InitialLoadDataRepositoryTest {

    private InitialLoadDataRepository initialLoadDataRepository;

    @BeforeEach
    void setUp() {
        this.initialLoadDataRepository = new InitialLoadDataRepository("src/test/java/com/safetynet/safetynetalerts/mockressources/initialmockdataset.json", MyAppConfig.objectMapper());
    }

    @Test
    void itShouldGetDataFromDataSet() {
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

        // When
        LoadInitialDataDTO actualDataset = initialLoadDataRepository.loadData();

        // Then
        assertThat(actualDataset).isEqualTo(mockDataSet);

    }

    @Test
    void itShouldThrowWhenFileNotFound() {
        // Given
        this.initialLoadDataRepository = new InitialLoadDataRepository("fake/file/dataset.json", MyAppConfig.objectMapper());

        // When
        // Then
        assertThatThrownBy(() -> initialLoadDataRepository.loadData())
                .hasMessageContaining("Server ERROR - impossible to find initial dataset")
                .isInstanceOf(ApiRepositoryException.class);
    }
}