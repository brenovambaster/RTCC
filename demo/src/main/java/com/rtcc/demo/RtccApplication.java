package com.rtcc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RtccApplication {

    public static void main(String[] args) {
        SpringApplication.run(RtccApplication.class, args);
    }

}
