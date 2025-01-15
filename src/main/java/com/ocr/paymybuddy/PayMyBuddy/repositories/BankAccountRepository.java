package com.ocr.paymybuddy.PayMyBuddy.repositories;

import com.ocr.paymybuddy.PayMyBuddy.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    BigDecimal findBalanceById(Integer accountId);

}


