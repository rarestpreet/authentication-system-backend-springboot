package com.project.authentication_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AuthenticationSystemApplication {

    public static void main(String[] args) {
        System.getenv("BASE_PATH");
        SpringApplication.run(AuthenticationSystemApplication.class, args);

    }

}
