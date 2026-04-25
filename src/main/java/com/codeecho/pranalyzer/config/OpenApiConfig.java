package com.codeecho.pranalyzer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * OpenAPI configuration for Swagger UI
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("CodeEcho API")
                .description("AI-Powered PR Pattern Analyzer & Code Generator - Revolutionizing development workflows through intelligent pattern recognition")
                .version("1.0.0")
                .contact(new Contact()
                    .name("CodeEcho Team")
                    .email("prashanth.kadubandi@sage.com, suresh.adiserla@sage.com")))
            .servers(new ArrayList<>()) // Empty servers list to hide dropdown
            .components(new Components().schemas(new HashMap<>())); // Empty schemas to hide models
    }
}
