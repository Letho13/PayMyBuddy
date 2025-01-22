package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.exceptions.TransactionException;
import com.ocr.paymybuddy.PayMyBuddy.mapper.ConnectionMapper;
import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserConnectionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.TransactionService;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.ConnectionDto;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.PerformTransactionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserConnectionRepository userConnectionRepository;
    private final UserRepository userRepository;
    private final ConnectionMapper connectionMapper;

    @GetMapping
    @Transactional(readOnly = true)
    public String transaction(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        com.ocr.paymybuddy.PayMyBuddy.models.User loggedInUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email " + email + " non trouvé"));

        List<UserConnection> connections = userConnectionRepository.findByFromUserEmail(loggedInUser.getEmail());

        List<ConnectionDto> connectionDtos = connectionMapper.connectionDtoFromConnection(connections);

        model.addAttribute("connections", connectionDtos);

        model.addAttribute("transaction", new PerformTransactionDto());

        return "transaction_page";
    }
//
//    @GetMapping("/showRelation")
//    private String showUserRelations(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//
//        com.ocr.paymybuddy.PayMyBuddy.models.User loggedInUser = userRepository.findUserByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email " + email + " non trouvé"));
//
//        List<UserConnection> connections = userConnectionRepository.findByTargetedUser(loggedInUser);
//
//        model.addAttribute("connections", connections);
//
//        return "transaction_page";
//    }


    @PostMapping()
    public ResponseEntity<String> handleTransaction(@ModelAttribute("transaction") PerformTransactionDto performTransactionDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            transactionService.performTransaction(performTransactionDto, currentUserEmail);
            return ResponseEntity.ok("Transaction réussie.");
        } catch (TransactionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

}
