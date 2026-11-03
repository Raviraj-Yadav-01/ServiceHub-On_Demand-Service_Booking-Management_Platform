package com.servicehub.apigateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.File;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {

		String envDir = new File("api-gateway/.env").exists() ? "api-gateway" : ".";

		Dotenv dotenv = Dotenv.configure().directory(envDir).ignoreIfMissing().load();

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}