package com.operis.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class OperisConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(OperisConfigApplication.class, args);
    }
}
