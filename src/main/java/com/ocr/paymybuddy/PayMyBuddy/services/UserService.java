package com.ocr.paymybuddy.PayMyBuddy.services;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.UserRegistrationDto;

import java.util.*;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);
    List<User> getAll();



}





