package com.safetynet.safetynetalerts.mockressources.utils;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.models.Person;

import java.util.ArrayList;
import java.util.List;

public class ChildAlertMockedData {
    public static ChildAlertDTO getMiniMaxime() {
        return new ChildAlertDTO(
                PersonsMockedData.getMiniMaxime().firstName(),
                PersonsMockedData.getMiniMaxime().lastName(),
                3,
                getOtherMembers()
        );
    }

    public static ChildAlertDTO getMiniAlireza() {
        return new ChildAlertDTO(
                PersonsMockedData.getMiniAlireza().firstName(),
                PersonsMockedData.getMiniAlireza().lastName(),
                0,
                getOtherMembers()
        );
    }

    private static List<Person> getOtherMembers() {
        List<Person> otherMembers = new ArrayList<>();

        Person maxime = PersonsMockedData.getMaxime();
        Person alireza = PersonsMockedData.getAlireza();

        otherMembers.add(maxime);
        otherMembers.add(alireza);

        return otherMembers;
    }
}
