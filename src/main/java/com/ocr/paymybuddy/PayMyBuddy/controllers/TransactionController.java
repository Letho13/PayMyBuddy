package com.ocr.paymybuddy.PayMyBuddy.controllers;

import com.ocr.paymybuddy.PayMyBuddy.models.Transaction;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

   private final TransactionService transactionService;

    @GetMapping()
    public String findAllTransactions(Model model){
       List<Transaction> transactions = transactionService.findAll();
        model.addAttribute("transactions",transactions);
        return "transactions-list";
    }

    @PostMapping()
    public String addTransaction (@RequestBody Transaction transaction, Model model) {
        transactionService.addTransaction(transaction);
        model.addAttribute("transaction");
        return "transaction_confirmation";
    }

}
