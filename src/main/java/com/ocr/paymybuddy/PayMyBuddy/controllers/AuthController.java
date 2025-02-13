package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.mapper.UserMapper;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.UserRegistrationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByUsername(userRegistrationDto.getUsername().toLowerCase()) != null ||
                userRepository.findUserByEmail(userRegistrationDto.getEmail().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        User myUser = userMapper.userFromDto(userRegistrationDto, passwordEncoder);

        return ResponseEntity.ok(userRepository.save(myUser));
    }

}

