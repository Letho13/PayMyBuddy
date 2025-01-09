package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.configuration.JwtUtils;
import com.ocr.paymybuddy.PayMyBuddy.mapper.UserMapper;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.UserRegistrationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByUsername(userRegistrationDto.getUsername().toLowerCase()) != null ||
                userRepository.findUserByEmail(userRegistrationDto.getEmail().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        User user = userMapper.userFromDto(userRegistrationDto, passwordEncoder);

        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", jwtUtils.generateToken(authentication.getName()));
                authData.put("type", "Bearer");
                return ResponseEntity.ok(authData);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


}

