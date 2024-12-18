package com.ocr.paymybuddy.PayMyBuddy.services;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll()
                .forEach(users::add);
        return users;
    }

    public User getUser(Integer id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public void addUser(User user){
        userRepository.save(user);
    }

}
