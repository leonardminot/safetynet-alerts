package com.safetynet.safetynetalerts.dto;
import com.safetynet.safetynetalerts.models.Person;

import java.util.List;

public record ChildAlertDTO(
        String firstName,
        String lastName,
        long age,
        List<Person> otherMembers

        ) {
}
