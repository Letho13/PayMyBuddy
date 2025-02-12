package com.ocr.paymybuddy.PayMyBuddy.controllers;


import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.UserServiceImplementation;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.AddRelationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserServiceImplementation userServiceImplementation;
    private final UserRepository userRepository;

    @GetMapping("/profil")
    public String profil(Model model, @RequestParam(value = "success", required = false) String success) {

        model.addAttribute("activePage", "profil");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userSecurity = (User) authentication.getPrincipal();

        com.ocr.paymybuddy.PayMyBuddy.models.User user = userRepository.findUserByEmail(userSecurity.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        if (success != null) {
            model.addAttribute("message", "Profil mis à jour avec succès !");
            model.addAttribute("messageType", "success");
        }

        return "profil_page";
    }

    @PostMapping("/profil/edit")
    public String updateUser(@ModelAttribute com.ocr.paymybuddy.PayMyBuddy.models.User user, @RequestParam(value = "password", required = false) String password, Model model) {
        try {
            userServiceImplementation.updateUser(user.getUsername(), user.getEmail(), password);
            return "redirect:/profil?success=true";
        } catch (Exception e) {
            model.addAttribute("message", "Erreur lors de la mise à jour du profil.");
            return "profil_page";
        }
    }

    @GetMapping("/add-relation")
    public String addRelation(Model model) {

        model.addAttribute("activePage", "add-relation");

        model.addAttribute("addRelationDto", new AddRelationDto());
        return "add_relation_page";
    }

    @PostMapping("/addConnection")
    public String addConnection(@ModelAttribute AddRelationDto addRelationDto, Model model) {
        try {

            userServiceImplementation.addUserConnection(addRelationDto.getEmailAddedUser(), getCurrentUserEmail());
            model.addAttribute("success", "Connexion ajoutée avec succès !");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "L'utilisateur est déjà lié au compte");

        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            log.error("ErrorMessage : {}", exception.getMessage());
        }
        return "add_relation_page";
    }

    private String getCurrentUserEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}





