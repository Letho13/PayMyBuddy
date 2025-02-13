package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.services.UserServiceImplementation;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserServiceImplementation userServiceImplementation;

    @GetMapping
    public String register() {
        return "register_page";
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDTO() {
        return new UserRegistrationDto();
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDTO) {

        try {
            userServiceImplementation.save(registrationDTO);
        } catch (DataIntegrityViolationException e) {
            log.error("Email already in use: {}", e.getMessage());
            return "redirect:/register?email_invalid";
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            return "redirect:/register?error";
        }
        return "redirect:/register?success";
    }

}
