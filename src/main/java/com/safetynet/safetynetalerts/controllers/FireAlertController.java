package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.services.FireAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fire")
@Slf4j
public class FireAlertController {
    private final FireAlertService fireAlertService;

    @Autowired
    public FireAlertController(FireAlertService fireAlertService) {
        this.fireAlertService = fireAlertService;
    }

    @GetMapping
    FireAlertDTO getFireAlert(@RequestParam("address") String address) {
        log.info("New request : GET /fire?address=" + address);
        return fireAlertService.getFireAlert(address);
    }
}
