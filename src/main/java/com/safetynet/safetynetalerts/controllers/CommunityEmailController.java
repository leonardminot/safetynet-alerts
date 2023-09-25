package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.services.CommunityEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("communityEmail")
@Slf4j
public class CommunityEmailController {

    private final CommunityEmailService communityEmailService;

    @Autowired
    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping
    List<String> getCommunityEmail(@RequestParam String city) {
        log.info("New request: GET /communityEmail?city=" + city);
        return communityEmailService.getEmail(city);
    }
}
