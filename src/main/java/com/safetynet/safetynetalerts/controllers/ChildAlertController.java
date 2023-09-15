package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.services.ChildAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("childAlert")
@Slf4j
public class ChildAlertController {
    private final ChildAlertService childAlertService;

    @Autowired
    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    @GetMapping
    List<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        log.info("New request : GET /childAlert?address=" + address);
        return childAlertService.getChildAlertAtAddress(address);
    }
}
