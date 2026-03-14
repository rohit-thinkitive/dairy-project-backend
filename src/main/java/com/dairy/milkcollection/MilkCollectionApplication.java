package com.dairy.milkcollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MilkCollectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(MilkCollectionApplication.class, args);
    }
}
