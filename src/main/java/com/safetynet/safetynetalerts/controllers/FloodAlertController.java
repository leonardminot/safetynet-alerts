package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.FloodAlertDTO;
import com.safetynet.safetynetalerts.services.FloodAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("flood")
@Slf4j
public class FloodAlertController {
    private final FloodAlertService floodAlertService;

    @Autowired
    public FloodAlertController(FloodAlertService floodAlertService) {
        this.floodAlertService = floodAlertService;
    }

    @GetMapping("stations")
    List<FloodAlertDTO> getFloodAlert(@RequestParam List<String> stations) {
        log.info("New request: GET /flood/stations?stations=" + stations);
        return floodAlertService.getFloodAlert(stations);
    }
}
