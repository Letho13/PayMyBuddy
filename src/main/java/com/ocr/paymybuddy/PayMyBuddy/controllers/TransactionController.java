package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.exceptions.TransactionException;
import com.ocr.paymybuddy.PayMyBuddy.services.TransactionService;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.PerformTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

   private final TransactionService transactionService;

   @GetMapping
   public String transaction() {
      return "transaction_page";
   }

   @PostMapping()
   public ResponseEntity<String> handleTransaction(@ModelAttribute("transaction")PerformTransactionDto performTransactionDto) {
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
