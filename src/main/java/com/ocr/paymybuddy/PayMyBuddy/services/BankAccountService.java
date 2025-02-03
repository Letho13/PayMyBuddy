package com.ocr.paymybuddy.PayMyBuddy.services;

import com.ocr.paymybuddy.PayMyBuddy.models.BankAccount;
import com.ocr.paymybuddy.PayMyBuddy.repositories.BankAccountRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;


    @Transactional
    public void addAmountToConnectedUserBankAccount(BigDecimal amount) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        com.ocr.paymybuddy.PayMyBuddy.models.User loggedInUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email " + email + " non trouvé"));

        // Récupérer le compte bancaire associé à l'utilisateur
        BankAccount bankAccount = loggedInUser.getBankAccount();
        if (bankAccount == null) {
            throw new IllegalStateException("No bank account associated with this user");
        }

        // Ajouter le montant au solde existant
        BigDecimal newBalance = bankAccount.getBalance().add(amount);
        bankAccount.setBalance(newBalance);

        // Sauvegarder les modifications
        bankAccountRepository.save(bankAccount);
    }

    public BigDecimal getBalanceForLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        com.ocr.paymybuddy.PayMyBuddy.models.User loggedInUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email " + email + " non trouvé"));

        // Vérifie si l'utilisateur a un compte bancaire
        BankAccount bankAccount = loggedInUser.getBankAccount();
        if (bankAccount == null) {
            throw new IllegalStateException("Aucun compte bancaire associé à cet utilisateur");
        }

        return bankAccount.getBalance();
    }
}


