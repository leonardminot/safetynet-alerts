package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiNotFoundException;
import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
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

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PersonRepository personRepository;

    @Captor
    private ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.medicalRecordService = new MedicalRecordService(new MedicalRecordMessageService(), medicalRecordRepository, personRepository);
    }

    @Test
    void itShouldCreateANewMedicalRecord() {
        // Given
        MedicalRecord alirezaRecord = MedicalRecordsMockedData.getAlirezaRecord();

        Person alirezaPerson = new Person(
                "Alireza",
                "Firouzja",
                null,
                null,
                null,
                null,
                null
        );

        // ... no medical record in the DB
        when(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        // ... and person exists in the DB
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

        // ... no medical record in the DB
        when(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        // ... but person doesn't exist in the DB
        when(personRepository.selectPersonByName(any(String.class), any(String.class))).thenReturn(Optional.empty());

        // When
        // Then
        medicalRecordRepository.getMedicalRecords();
        assertThatThrownBy(() -> medicalRecordService.createRecord(unknownPersonMedicalRecord))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining(
                        String.format("Impossible to create: [%s] - Error: Person with name [%s %s] does not exist",
                                unknownPersonMedicalRecord,
                                unknownPersonMedicalRecord.firstName(),
                                unknownPersonMedicalRecord.lastName())
                );
        then(medicalRecordRepository).should(never()).saveRecord(any(MedicalRecord.class));
    }

    @Test
    void itShouldThrowWhenMedicalRecordAlreadyExists() {
        // When
        MedicalRecord currentRecord = MedicalRecordsMockedData.getMagnusRecord();

        // ... already a medical record in the DB
        when(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).thenReturn(Optional.of(currentRecord));

        // Then
        // When
        assertThatThrownBy(() -> medicalRecordService.createRecord(currentRecord))
                .isInstanceOf(ApiResourceException.class)
                .hasMessageContaining(
                        String.format("Impossible to create: [%s] - Error: Medical record for [%s %s] already exists",
                                currentRecord,
                                currentRecord.firstName(),
                                currentRecord.lastName())
                );
        then(medicalRecordRepository).should(never()).saveRecord(any(MedicalRecord.class));

    }

    @Test
    void itShouldThrowWhenBirthdateInFuture() {
        // Given
        MedicalRecord medicalRecordWithBirthdateInFuture = new MedicalRecord(
                "Wesley",
                "So",
                LocalDate.parse("2293-10-09"),
                null,
                null
        );

        // When
        // Then
        assertThatThrownBy(() -> medicalRecordService.createRecord(medicalRecordWithBirthdateInFuture))
                .isInstanceOf(ApiResourceException.class)
                .hasMessageContaining(
                        String.format("Impossible to create: [%s] - Error: Birthdate for [%s %s] is in the future: [%s]",
                                medicalRecordWithBirthdateInFuture,
                                medicalRecordWithBirthdateInFuture.firstName(),
                                medicalRecordWithBirthdateInFuture.lastName(),
                                medicalRecordWithBirthdateInFuture.birthdate())
                );
        then(medicalRecordRepository).should(never()).saveRecord(any(MedicalRecord.class));

    }

    @Test
    void itShouldUpdateAMedicalRecord() {
        // Given
        // ... the current record
        MedicalRecord currentRecord = MedicalRecordsMockedData.getMagnusRecord();

        // ... the update request
        MedicalRecord updateRequest = new MedicalRecord(
                "Magnus",
                "Carlsen",
                null,
                List.of("aznol:350mg", "hydrapermazol:100mg", "ketamine:1000mg"),
                List.of("nillacilan","peanut butter")
        );

        when(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).thenReturn(Optional.of(currentRecord));


        // When
        medicalRecordService.update(updateRequest);

        // Then
        then(medicalRecordRepository).should().update(medicalRecordArgumentCaptor.capture());
        assertThat(medicalRecordArgumentCaptor.getValue()).isEqualTo(updateRequest);
    }

    @Test
    void itShouldThrowWhenMedicalRecordIsNotFound() {
        // Given
        MedicalRecord unknownPersonMedicalRecord = new MedicalRecord(
                "Wesley",
                "So",
                LocalDate.parse("1993-10-09"),
                null,
                null
        );

        when(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> medicalRecordService.update(unknownPersonMedicalRecord))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining(
                        String.format("Impossible to update: [%s] - Error: Medical Record for [%s %s] does not exist",
                                unknownPersonMedicalRecord,
                                unknownPersonMedicalRecord.firstName(),
                                unknownPersonMedicalRecord.lastName())
                );
        then(medicalRecordRepository).should(never()).update(any(MedicalRecord.class));
    }

    @Test
    void itShouldDeleteAMedicalRecord() {
        // Given
        MedicalRecord recordToDelete = new MedicalRecord(
                "Magnus",
                "Carlsen",
                null,
                null,
                null
        );

        when(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).thenReturn(Optional.of(recordToDelete));

        // When
        medicalRecordService.delete(recordToDelete);

        // Then
        then(medicalRecordRepository).should().delete(medicalRecordArgumentCaptor.capture());
        assertThat(medicalRecordArgumentCaptor.getValue()).isEqualTo(recordToDelete);
    }

    @Test
    void itShouldThrowWhenNameNotFoundAndNotDeleteMedicalRecord() {
        // Given
        MedicalRecord unknownPersonMedicalRecord = new MedicalRecord(
                "Wesley",
                "So",
                LocalDate.parse("1993-10-09"),
                null,
                null
        );

        when(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> medicalRecordService.delete(unknownPersonMedicalRecord))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining(
                        String.format("Impossible to delete: [%s] - Error: Medical Record for [%s %s] does not exist",
                                unknownPersonMedicalRecord,
                                unknownPersonMedicalRecord.firstName(),
                                unknownPersonMedicalRecord.lastName())
                );
        then(medicalRecordRepository).should(never()).delete(any(MedicalRecord.class));
    }


}