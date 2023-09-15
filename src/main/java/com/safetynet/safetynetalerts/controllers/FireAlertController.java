package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.services.FireAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fire")
public class FireAlertController {
    private final FireAlertService fireAlertService;

    @Autowired
    public FireAlertController(FireAlertService fireAlertService) {
        this.fireAlertService = fireAlertService;
    }

    @GetMapping
    FireAlertDTO getFireAlert(@RequestParam("address") String address) {
        return fireAlertService.getPersonsAtAddress(address);
    }
}
