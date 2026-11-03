package com.servicehub.userservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;


@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {

        String envDir = new File("user-service/.env").exists() ? "user-service" : ".";

        Dotenv dotenv = Dotenv.configure().directory(envDir).ignoreIfMissing().load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(UserServiceApplication.class, args);
    }
}
