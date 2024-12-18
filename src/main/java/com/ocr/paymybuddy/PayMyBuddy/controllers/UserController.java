package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/welcome")
public class UserController {

    private final UserService userService;

//    @GetMapping()
//    public String home(Model model) {
//        String example = userService.findUsernameByEmail("example");
//        model.addAttribute("example", example);
//        return "Welcome";
//    }

}
