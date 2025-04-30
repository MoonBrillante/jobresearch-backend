package com.example.jobresearch.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobSearchOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Search API")
                        .description("API documentation for job application system")
                        .version("1.0"));
    }
}