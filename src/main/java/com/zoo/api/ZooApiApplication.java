package com.zoo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;

import com.zoo.api.ZooApiApplication;

@SpringBootApplication(scanBasePackages = "com.zoo.api", exclude = {org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class})

public class ZooApiApplication {

	public static void main(String[] args) {
		 SpringApplication.run(ZooApiApplication.class, args);
	}
}
