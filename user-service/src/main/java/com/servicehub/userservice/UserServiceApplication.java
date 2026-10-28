package com.servicehub.userservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {

        Dotenv.configure().ignoreIfMissing().systemProperties().load();

        SpringApplication.run(UserServiceApplication.class, args);
    }
}
