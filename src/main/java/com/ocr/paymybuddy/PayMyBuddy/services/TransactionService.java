package com.ocr.paymybuddy.PayMyBuddy.services;

import com.ocr.paymybuddy.PayMyBuddy.models.Transaction;
import com.ocr.paymybuddy.PayMyBuddy.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> findAll(){

        return transactionRepository.findAll();
    }

    public void addTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }



}
