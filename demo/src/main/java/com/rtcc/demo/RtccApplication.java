package com.rtcc.demo;

import com.rtcc.demo.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class RtccApplication {

public static void main(String[] args) {
		SpringApplication.run(RtccApplication.class, args);
	}

}
