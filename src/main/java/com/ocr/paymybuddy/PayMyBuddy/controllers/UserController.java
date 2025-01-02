package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class UserController {

    private final UserService userService;


    @GetMapping("/users/{email}")
    public String findUserByEmail (@PathVariable String email, Model model) {
        Optional<User> user = userService.findUserByEmail(email);
        model.addAttribute("users",user);
        return "user_page";
    }

    @PostMapping("/user")
    public String addUser (@RequestBody User user, Model model) {
        userService.addUser(user);
        model.addAttribute("user");
        return "user_confirmation";
    }
}
