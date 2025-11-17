package com.example.cloudvendor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);

    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("Initializing OpenAPI configuration");
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development Server")
                ))
                .info(new Info()
                        .title("Cloud Vendor API")
                        .description("REST API for managing cloud vendors with full CRUD operations")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Cloud Vendor Team")
                                .email("support@cloudvendor.com")
                                .url("https://cloudvendor.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
