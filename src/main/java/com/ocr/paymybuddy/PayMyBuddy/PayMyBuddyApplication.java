package com.ocr.paymybuddy.PayMyBuddy;

import com.github.javafaker.Faker;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.repositories.TransactionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PayMyBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
    }

    @Bean

    public CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            TransactionRepository transactionRepository
    ){
        return args -> {
//            for (int i =0;i<50;i++){
//                Faker faker = new Faker();
//                var user = User.builder()
//                        .username(faker.name().username())
//                        .password(faker.internet().password())
//                        .email(faker.internet().emailAddress())
//                        .build();
//                userRepository.save(user);
//            }

        };
    }

}
