package com.safetynet.safetynetalerts.configuration;

import com.safetynet.safetynetalerts.services.InitialLoadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class LoadInitialDataRunner implements CommandLineRunner {

    private final InitialLoadDataService initialLoadDataService;

    @Autowired
    public LoadInitialDataRunner(InitialLoadDataService initialLoadDataService) {
        this.initialLoadDataService = initialLoadDataService;
    }

    @Override
    public void run(String... args) {
        initialLoadDataService.initializeData();
    }
}
