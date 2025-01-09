package com.ocr.paymybuddy.PayMyBuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ocr.paymybuddy.PayMyBuddy.repositories")
public class PayMyBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
    }

}
