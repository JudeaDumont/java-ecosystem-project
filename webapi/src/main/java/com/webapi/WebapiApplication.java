package com.webapi;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
@EnableEncryptableProperties
public class WebapiApplication {

    //todo: create/migrate to a separate "troubleshooting.md" file
    //todo: move to jersey servlet
    //todo: use mocking tests!
    //todo: setup kafka pub/sub
    //todo: look up what other techs FB wants, and build those in
    //todo: build out whole fullstack testing + infra in github actions for whol stack.

    public static void main(String[] args) {
        SpringApplication.run(WebapiApplication.class, args);
    }

    @Bean
    @SuppressWarnings("unused")
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000", //React
                "http://localhost:4200" //Angular
        ));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                "Origin",
                "Access-Control-Allow-Origin",
                "Content-Type",
                "Accept",
                "Jwt-Token",
                "Authorization",
                "Origin",
                "Accept",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        corsConfiguration.setExposedHeaders(Arrays.asList(
                "Origin",
                "Content-Type",
                "Accept",
                "Jwt-Token",
                "Authorization",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Filename"));
        corsConfiguration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}

//todo: too much output in github actions CI/CD

//todo: figure out the best way to deal with environment variables, like the secret key for jasypt for instance
// right now they are dealt with by editing: run configurations... > templates > add env var there

//todo: Tools to use:
// assertj
// mockito

//todo: to DRY out all the code in this repo, what we need to do is refactor into
// method enclosures that are no more than three lines long

//todo: top down refactor of code, naming conventions, function enclosures, etc.

//todo: look at the correct way to use hibernate from that spring boot postgres tutorial

//todo: add a react front-end

//todo: add selenium tests

//todo: add mockito

//todo: add documentation on test automation fundamentals and how to prioritize automation tasks

//todo: add CI/CD hooked into git hub, gitLab

//todo: add code coverage tools (guide, not a goal) SonarQube

//todo: encrypt the information in github actions and maven setting

//todo: encrypt the credentials in the pom file (its annoying because you have to use maven settings that are external to the project)

//todo: add an enum to the schema, use flyway, integrate all the way up into UI

//todo: refactor all 3 repos, starting with minimizing github actions workflow