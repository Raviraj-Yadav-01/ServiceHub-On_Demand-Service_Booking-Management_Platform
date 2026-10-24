package com.servicehub.apigateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {

		Dotenv.configure()
				.ignoreIfMissing()
				.systemProperties()
				.load();
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}