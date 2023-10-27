package com.safetynet.safetynetalerts.configuration;

import com.safetynet.safetynetalerts.services.InitialLoadDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!dev")
public class LoadInitialDataRunner implements CommandLineRunner {

    private final InitialLoadDataService initialLoadDataService;

    public LoadInitialDataRunner(InitialLoadDataService initialLoadDataService) {
        this.initialLoadDataService = initialLoadDataService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Execution Command Line Runner");
        initialLoadDataService.initializeData();
    }
}
