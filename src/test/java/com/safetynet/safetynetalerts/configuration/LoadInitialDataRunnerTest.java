package com.safetynet.safetynetalerts.configuration;

import com.safetynet.safetynetalerts.mockressources.utils.ManageMockedData;
import com.safetynet.safetynetalerts.services.InitialLoadDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("dev")
public class LoadInitialDataRunnerTest implements CommandLineRunner {
    private final InitialLoadDataService initialLoadDataService;

    private final String filePath;

    public LoadInitialDataRunnerTest(InitialLoadDataService initialLoadDataService,@Value("${safetynetalerts.jsonpath.dataset}") String filePath) {
        this.initialLoadDataService = initialLoadDataService;
        this.filePath = filePath;
    }

    @Override
    public void run(String... args) throws IOException {
        ManageMockedData.createMockedDataWithAllEntries(filePath);
        initialLoadDataService.initializeData();
    }
}
