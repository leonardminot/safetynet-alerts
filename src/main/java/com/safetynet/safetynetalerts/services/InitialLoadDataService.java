package com.safetynet.safetynetalerts.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InitialLoadDataService {

    public InitialLoadDataService(@Value("${safetynetalerts.jsonpath.dataset}") String filePath, ObjectMapper objectMapper) {
    }
}
