package com.example.CertVerify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan(basePackages = "com.example.models")
@EnableJpaRepositories(basePackages = "com.example.repository")
public class CertVerifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertVerifyApplication.class, args);
	}

}
