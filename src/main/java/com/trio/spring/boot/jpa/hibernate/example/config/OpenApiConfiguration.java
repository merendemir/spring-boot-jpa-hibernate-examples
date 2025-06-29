package com.trio.spring.boot.jpa.hibernate.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot JPA Hibernate Example API")
                        .version("1.0")
                        .description("API documentation for Spring Boot JPA Hibernate Example project"));
    }
}