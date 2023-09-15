package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.services.PhoneAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("phoneAlert")
@Slf4j
public class PhoneAlertController {
    private final PhoneAlertService phoneAlertService;

    @Autowired
    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping
    public List<String> getPhoneAlert(@RequestParam("firestation") String firestation) {
        log.info("New request : GET /phoneAlert?firestation=" + firestation);
        return phoneAlertService.getPhoneNumbersForFireStation(firestation);
    }
}
