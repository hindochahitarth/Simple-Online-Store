package org.example.simpleonlinestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SimpleOnlineStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleOnlineStoreApplication.class, args);
    }

}