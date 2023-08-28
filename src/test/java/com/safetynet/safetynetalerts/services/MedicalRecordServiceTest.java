package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    private MedicalRecordService medicalRecordService;

    private final String filePathMockPersons = "src/test/java/com/safetynet/safetynetalerts/mockressources/mockmedicalrecords.json";

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PersonRepository personRepository;

    @Captor
    private ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.medicalRecordService = new MedicalRecordService(medicalRecordRepository, personRepository);
    }

    @Test
    void itShouldCreateANewMedicalRecord() {
        // Given
        MedicalRecord alirezaRecord = new MedicalRecord(
                "Alireza",
                "Firouzja",
                LocalDate.parse("2003-06-18"),
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );

        Person alirezaPerson = new Person(
                "Alireza",
                "Firouzja",
                null,
                null,
                null,
                null,
                null
        );

        given(personRepository.selectPersonByName(alirezaRecord.firstName(), alirezaRecord.lastName())).willReturn(Optional.of(alirezaPerson));

        // When
        medicalRecordService.createRecord(alirezaRecord);

        // Then
        then(medicalRecordRepository).should().saveRecord(medicalRecordArgumentCaptor.capture());
        assertThat(medicalRecordArgumentCaptor.getValue()).isEqualTo(alirezaRecord);
    }

    @Test
    void itShouldThrowWhenUnknownPerson() {
        // Given
        MedicalRecord unknownPersonMedicalRecord = new MedicalRecord(
                "Wesley",
                "So",
                LocalDate.parse("1993-10-09"),
                null,
                null
        );

        when(personRepository.selectPersonByName(any(String.class), any(String.class))).thenReturn(Optional.empty());

        // When
        // Then
        medicalRecordRepository.getMedicalRecords();
        assertThatThrownBy(() -> medicalRecordService.createRecord(unknownPersonMedicalRecord))
                .isInstanceOf(ApiResourceException.class)
                .hasMessageContaining(
                        String.format("Impossible to create Medical Record for %s %s : unknown person",
                                unknownPersonMedicalRecord.firstName(),
                                unknownPersonMedicalRecord.lastName())
                );
        then(medicalRecordRepository).should(never()).saveRecord(any(MedicalRecord.class));
    }
}