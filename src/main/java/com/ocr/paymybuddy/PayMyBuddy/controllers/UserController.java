package com.ocr.paymybuddy.PayMyBuddy.controllers;


import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.UserServiceImplementation;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.AddRelationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserServiceImplementation userServiceImplementation;
    private final UserRepository userRepository;

    @GetMapping("/profil")
    public String profil(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", userRepository.findUserByEmail(user.getUsername()).get());

        return "profil_page";
    }

    @GetMapping("/add-relation")
    public String addRelation(Model model) {
        model.addAttribute("addRelationDTO", new AddRelationDTO());
        return "add_relation_page";
    }

    @PostMapping("/addConnection")
    public String addConnection(@ModelAttribute AddRelationDTO addRelationDTO, Model model) {
        try {
            // Appel au service pour ajouter la connexion
            userServiceImplementation.addUserConnection(addRelationDTO.getEmailAddedUser(), getCurrentUserEmail());
            model.addAttribute("success", "Connexion ajoutée avec succès !");
        } catch (Exception e) {
            model.addAttribute("error", "L'email spécifié n'existe pas.");
            log.error("connection failed",e);
        }
        return "add_relation_page";
    }

    private String getCurrentUserEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}





