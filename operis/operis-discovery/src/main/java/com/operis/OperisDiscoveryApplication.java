package com.operis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer // Active les fonctionnalités de serveur de découverte
@SpringBootApplication // Plus complet que @SpringBootConfiguration
public class OperisDiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(OperisDiscoveryApplication.class, args);
    }
}