package de.dominikschadow.javasecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Requires the configuration of @code{githubClient.client.clientId} and @code{githubClient.client.clientSecret} as runtime parameter.
 * This project is based on the Spring Boot and OAuth2 tutorial available at https://spring.io/guides/tutorials/spring-boot-oauth2
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
