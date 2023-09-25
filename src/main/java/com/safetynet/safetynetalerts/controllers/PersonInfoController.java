package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.services.PersonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("personInfo")
@Slf4j
public class PersonInfoController {

    private PersonInfoService personInfoService;

    @Autowired
    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @GetMapping
    private List<PersonInfoDTO> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        log.info("New request: GET /personInfo?firstName=" + firstName + "&lastName=" + lastName);
        return personInfoService.getPersonInfo(firstName, lastName);
    }
}
