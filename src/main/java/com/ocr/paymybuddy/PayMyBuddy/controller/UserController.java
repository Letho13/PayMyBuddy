package com.ocr.paymybuddy.PayMyBuddy.controller;

import com.ocr.paymybuddy.PayMyBuddy.repository.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
