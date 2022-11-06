package com.webapi;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class WebapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebapiApplication.class, args);
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