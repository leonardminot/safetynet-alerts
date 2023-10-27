package com.safetynet.safetynetalerts.configuration;

import com.safetynet.safetynetalerts.services.InitialLoadDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadInitialDataRunner implements CommandLineRunner {

    private final InitialLoadDataService initialLoadDataService;

    public LoadInitialDataRunner(InitialLoadDataService initialLoadDataService) {
        this.initialLoadDataService = initialLoadDataService;
    }

    @Override
    public void run(String... args) {
        initialLoadDataService.initializeData();
    }
}
