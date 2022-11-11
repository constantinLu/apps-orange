package com.orange.corepayments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CorePaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorePaymentsApplication.class, args);
    }
}
