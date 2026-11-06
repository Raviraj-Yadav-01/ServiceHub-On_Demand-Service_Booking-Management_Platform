package com.servicehub.notificationservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {

        String envDir = new File("notification-service/.env").exists() ? "notification-service" : ".";

        Dotenv dotenv = Dotenv.configure().directory(envDir).ignoreIfMissing().load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
