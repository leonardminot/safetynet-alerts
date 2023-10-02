package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.exception.ApiNotFoundException;
import com.safetynet.safetynetalerts.exception.ApiResourceException;
import com.safetynet.safetynetalerts.mockressources.utils.MedicalRecordsMockedData;
import com.safetynet.safetynetalerts.mockressources.utils.PersonsMockedData;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;
import com.safetynet.safetynetalerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

    @Captor
    private ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor;

    @BeforeEach
    void setUp() {
        personService = new PersonService(personRepository, new PersonMessageService(), medicalRecordRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void itShouldCreateANewPerson() {
        // Given
        Person person = new Person(
                "Harry",
                "Potter",
                "Gryfondor Poudlard",
                "Pré-au-lard",
                "777777",
                "123-456-7890",
                "harry.potter@poudlard.fr"
                );


        // When
        personService.createPerson(person);

        // Then
        then(personRepository).should().savePerson(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(person);

    }

    @Test
    void itShouldThrowWhenPersonCreateAndAlreadyExists() {
        // Given
        Person person = new Person(
                "Harry",
                "Potter",
                "Gryfondor Poudlard",
                "Pré-au-lard",
                "777777",
                "123-456-7890",
                "harry.potter@poudlard.fr"
        );

        given(personRepository.selectPersonByName(person.firstName(), person.lastName())).willReturn(Optional.of(person));

        // When
        // Then
        assertThatThrownBy(() -> personService.createPerson(person))
                .isInstanceOf(ApiResourceException.class)
                .hasMessageContaining(
                        String.format("POST /person - Payload: [%s] - Error: Person with name [%s %s] already exists",
                                person,
                                person.firstName(),
                                person.lastName()));
        then(personRepository).should(never()).savePerson(any(Person.class));
    }

    @Test
    void itShouldUpdateAPerson() {
        // Given
        // ... an existing person
        Person maximeExisting = PersonsMockedData.getMaxime();

        // ... A person in the DB with new values for email and phone number

        Person maximeToUpdate = new Person(
                "Maxime",
                "Vachier-Lagrave",
                "1990 Rue de la Tour",
                "Paris",
                "75001",
                "111-222-3333",
                "maxime.vachierlagrave@email.com"
        );
        // ... and the Person is found
        when(personRepository.selectPersonByName(any(String.class), any(String.class))).thenReturn(Optional.of(maximeExisting));

        // When
        personService.updatePerson(maximeToUpdate);

        // Then
        then(personRepository).should().update(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(maximeToUpdate);
    }

    @Test
    void itShouldThrownWhenUpdateAndPersonIsNotFound() {
        // Given
        Person personThatDoesntExist = new Person(
                "Harry",
                "Potter",
                "Gryfondor Poudlard",
                "Pré-au-lard",
                "777777",
                "123-456-7890",
                "harry.potter@poudlard.fr"
        );

        // ... and person is not found
        given(personRepository.selectPersonByName(any(String.class), any(String.class))).willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> personService.updatePerson(personThatDoesntExist))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining(String.format("PUT /person - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                        personThatDoesntExist,
                        personThatDoesntExist.firstName(),
                        personThatDoesntExist.lastName()));
        then(personRepository).should(never()).update(any(Person.class));
    }

    @Test
    void itShouldDeleteAPerson() {
        // Given
        Person personToDelete = new Person(
                "Maxime",
                "Vachier-Lagrave",
                null,
                null,
                null,
                null,
                null
        );
        // ... and the Person is found
        given(personRepository.selectPersonByName(any(String.class), any(String.class))).willReturn(Optional.of(personToDelete));

        // When
        personService.delete(personToDelete);

        // Then
        then(personRepository).should().delete(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(personToDelete);
    }

    @Test
    void itShouldDeleteAPersonAndHisMedicalRecordIfPresent() {
        // Given
        Person personToDelete = new Person(
                "Maxime",
                "Vachier-Lagrave",
                null,
                null,
                null,
                null,
                null
        );

        MedicalRecord personToDeleteMedicalRecord = MedicalRecordsMockedData.getMaximeRecord();

        // ... and the Person is found
        given(personRepository.selectPersonByName(any(String.class), any(String.class))).willReturn(Optional.of(personToDelete));
        // ... and his Medical Record is found
        given(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).willReturn(Optional.of(personToDeleteMedicalRecord));

        // When
        personService.delete(personToDelete);

        // Then
        then(medicalRecordRepository).should().delete(medicalRecordArgumentCaptor.capture());
        assertThat(medicalRecordArgumentCaptor.getValue()).isEqualTo(personToDeleteMedicalRecord);
    }

    @Test
    void itShouldDeleteAPersonAndNotHisMedicalRecordIfNotPresent() {
        // Given
        Person personToDelete = new Person(
                "Maxime",
                "Vachier-Lagrave",
                null,
                null,
                null,
                null,
                null
        );

        // ... and the Person is found
        given(personRepository.selectPersonByName(any(String.class), any(String.class))).willReturn(Optional.of(personToDelete));
        // ... and his Medical Record is not found
        given(medicalRecordRepository.selectMedicalRecordByName(any(String.class), any(String.class))).willReturn(Optional.empty());

        // When
        personService.delete(personToDelete);

        // Then
        then(medicalRecordRepository).should(never()).delete(any(MedicalRecord.class));
        then(personRepository).should().delete(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(personToDelete);
    }

    @Test
    void itShouldThrowWhenDeleteAndPersonIsNotFound() {
        // Given
        Person personThatDoesntExist = new Person(
                "Harry",
                "Potter",
                null,
                null,
                null,
                null,
                null
        );

        // ... and the person is not found
        given(personRepository.selectPersonByName(any(String.class), any(String.class))).willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> personService.delete(personThatDoesntExist))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining(String.format("DELETE /person - Payload: [%s] - Error: Person with name [%s %s] does not exist",
                        personThatDoesntExist,
                        personThatDoesntExist.firstName(),
                        personThatDoesntExist.lastName()));
        then(personRepository).should(never()).delete(any(Person.class));
    }
}