package com.ocr.paymybuddy.PayMyBuddy;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testFindUserByEmail() {
        Optional<User> user = userRepository.findUserByEmail("theo@test2.com");

        if (user.isPresent()) {
            System.out.println("Utilisateur trouvé : " + user.get());
        } else {
            System.out.println("Aucun utilisateur trouvé avec l'email : theo@test2.com");
        }

        assertTrue("",user.isPresent());
    }

}
