package com.sicofark.sicofark_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
	"com.sicofark.sicofark_backend",
	"persistence",
	"service",
	"presentation"
})
@EnableJpaRepositories(basePackages = "persistence.repository")
@EntityScan(basePackages = "persistence.entity")
public class SicofarkBackendApplication {

	public static void main(String[] args) {
        SpringApplication.run(SicofarkBackendApplication.class, args);
	}

}
