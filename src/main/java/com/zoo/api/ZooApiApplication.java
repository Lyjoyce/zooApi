package com.zoo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zoo.api.ZooApiApplication;

@SpringBootApplication
public class ZooApiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ZooApiApplication.class);

		app.setAdditionalProfiles("dev");
		app.run(args);
	}
}
