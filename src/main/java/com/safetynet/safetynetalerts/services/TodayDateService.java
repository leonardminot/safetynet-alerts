package com.safetynet.safetynetalerts.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TodayDateService {
    public LocalDate getNow() {
        return LocalDate.now();
    }
}
