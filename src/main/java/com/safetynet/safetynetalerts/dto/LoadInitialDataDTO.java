package com.safetynet.safetynetalerts.dto;

import com.safetynet.safetynetalerts.models.Firestation;
import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;

import java.util.List;

public record LoadInitialDataDTO(
        List<Person> persons,
        List<Firestation> firestations,
        List<MedicalRecord> medicalrecords
) {
}
