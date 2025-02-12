package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.exceptions.TransactionException;
import com.ocr.paymybuddy.PayMyBuddy.mapper.ConnectionMapper;
import com.ocr.paymybuddy.PayMyBuddy.models.Transaction;
import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserConnectionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.BankAccountService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserConnectionRepository userConnectionRepository;
    private final UserRepository userRepository;
    private final ConnectionMapper connectionMapper;
    private final BankAccountService bankAccountService;

    @GetMapping
    @Transactional(readOnly = true)
    public String transaction(Model model) {

        model.addAttribute("activePage", "transaction");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        com.ocr.paymybuddy.PayMyBuddy.models.User loggedInUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email " + email + " non trouvé"));

        model.addAttribute("username", loggedInUser.getUsername());

        List<UserConnection> connections = userConnectionRepository.findByFromUserEmail(loggedInUser.getEmail());
        List<ConnectionDto> connectionDtos = connectionMapper.connectionDtoFromConnection(connections);
        BigDecimal balance = bankAccountService.getBalanceForLoggedInUser();

        List<Transaction> transactions = transactionService.getTransactionsForUser(email);

        model.addAttribute("connections", connectionDtos);
        model.addAttribute("transactions", transactions);
        model.addAttribute("transaction", new PerformTransactionDto());
        model.addAttribute("balance", balance);

        return "transaction_page";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleTransaction(@ModelAttribute("transaction") PerformTransactionDto performTransactionDto) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            transactionService.performTransaction(performTransactionDto, currentUserEmail);

            response.put("messageType", "success");
            response.put("message", "Transaction réussie !");
            return ResponseEntity.ok(response);
        } catch (TransactionException e) {
            response.put("messageType", "danger");
            response.put("message", "Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/addAmount")
    public String addAmountToBankAccount(@RequestParam BigDecimal amount, Model model) {
        try {

            bankAccountService.addAmountToConnectedUserBankAccount(amount);

            model.addAttribute("successMessage", "Montant ajouté avec succès !");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/transaction";
    }

}
