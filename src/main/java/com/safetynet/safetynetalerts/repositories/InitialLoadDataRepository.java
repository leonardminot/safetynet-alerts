package com.safetynet.safetynetalerts.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.LoadInitialDataDTO;
import com.safetynet.safetynetalerts.exception.ApiRepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
@Slf4j
public class InitialLoadDataRepository {

    private final String filePath;
    private final ObjectMapper objectMapper;

    @Autowired
    public InitialLoadDataRepository(@Value("${safetynetalerts.jsonpath.dataset}") String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
    }


    public LoadInitialDataDTO loadData() {
        try {
            Path path = Paths.get(this.filePath);
            if (Files.size(path) != 0)
                return objectMapper.readValue(path.toFile(), new TypeReference<>() {});

        } catch (IOException e) {
            log.error("Server ERROR - impossible to find initial dataset");
            throw new ApiRepositoryException("Server ERROR - impossible to find initial dataset");
        }
        return null;
    }
}
