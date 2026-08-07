package com.destleiloes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DestLeiloesApplication {
    public static void main(String[] args) {
        SpringApplication.run(DestLeiloesApplication.class, args);
    }
}
