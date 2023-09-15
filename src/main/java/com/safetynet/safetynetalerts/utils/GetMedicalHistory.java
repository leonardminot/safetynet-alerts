package com.safetynet.safetynetalerts.utils;

import com.safetynet.safetynetalerts.models.MedicalRecord;
import com.safetynet.safetynetalerts.models.Person;

import java.util.List;

public class GetMedicalHistory {
    public static List<String> getMedications(List<MedicalRecord> medicalRecords, Person person) {
        return medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(person.firstName()) && mr.lastName().equals(person.lastName()))
                .findFirst()
                .map(MedicalRecord::medications)
                .orElse(List.of());
    }

    public static List<String> getAllergies(List<MedicalRecord> medicalRecords, Person person) {
        return medicalRecords.stream()
                .filter(mr -> mr.firstName().equals(person.firstName()) && mr.lastName().equals(person.lastName()))
                .findFirst()
                .map(MedicalRecord::allergies)
                .orElse(List.of());
    }
}
