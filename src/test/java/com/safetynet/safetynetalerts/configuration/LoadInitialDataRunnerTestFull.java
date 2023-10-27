package com.safetynet.safetynetalerts.configuration;

import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class LoadInitialDataRunnerTestFull implements CommandLineRunner {

    private final String filePathMockData;

    public LoadInitialDataRunnerTestFull(@Value("${safetynetalerts.jsonpath.dataset}") String filePathMockData) {
        this.filePathMockData = filePathMockData;
    }

    @Override
    public void run(String... args) throws Exception {
        ManageMockedData.createMockedDataWithAllEntries(this.filePathMockData);
    }
}
