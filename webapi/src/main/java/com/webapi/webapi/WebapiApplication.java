package com.webapi.webapi;

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

//todo: figure out the best way to deal with environment variables, like the secret key for jasypt for instance
// right now they are dealt with by editing: run configurations... > templates > add env var there

//todo: Tools to use:
// assertj
// mockito
