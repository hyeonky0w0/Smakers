package com.example.smakersbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SmakersBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmakersBeApplication.class, args);
    }

}
