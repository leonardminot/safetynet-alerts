package com.safetynet.safetynetalerts.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Léonard MINOT",
                        email = "leonard.minot@gadz.org"
                ),
                description = "SafetyNet Alerts API documentation. This is an application designed to help emergency services understand and respond to all situations as quickly and effectively as possible, with the aim of improving the timeliness and efficiency of emergency response.",
                title = "SafetyNet Alerts",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )

        }
)
public class OpenApiConfig {
}
