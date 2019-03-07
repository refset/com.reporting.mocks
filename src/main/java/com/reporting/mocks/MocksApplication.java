package com.reporting.mocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.reporting" )
public class MocksApplication {
	public static void main(String[] args) {
		SpringApplication.run(MocksApplication.class, args);
	}
}
