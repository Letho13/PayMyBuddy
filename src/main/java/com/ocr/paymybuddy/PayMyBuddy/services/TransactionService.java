package com.ocr.paymybuddy.PayMyBuddy.services;

import com.ocr.paymybuddy.PayMyBuddy.constant.Fare;
import com.ocr.paymybuddy.PayMyBuddy.exceptions.TransactionException;
import com.ocr.paymybuddy.PayMyBuddy.models.Transaction;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.repositories.BankAccountRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.TransactionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.PerformTransactionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;


    @Transactional(rollbackFor = {TransactionException.class})
    public void performTransaction(PerformTransactionDto performTransactionDto, String emailCurrentUser) throws TransactionException {

        BigDecimal amountTransaction = performTransactionDto.getAmount();

        User currentUser = userRepository.findUserByEmail(emailCurrentUser).get();
        User beneficiaryUser = userRepository.findUserByEmail(performTransactionDto.getEmailBeneficiary()).get();

        BigDecimal balanceCurrentUser = currentUser.getBankAccount().getBalance();
        if (balanceCurrentUser.compareTo(amountTransaction) < 0) {
            throw new TransactionException("Solde insuffisant");
        }

        BigDecimal newBalanceCurrentUser = currentUser.getBankAccount().getBalance()
                .subtract(amountTransaction)
                .subtract(amountTransaction.multiply(Fare.fareBytransaction));

        BigDecimal newBalanceBeneficiary = addOperation(beneficiaryUser, amountTransaction);

        currentUser.getBankAccount().setBalance(newBalanceCurrentUser);
        beneficiaryUser.getBankAccount().setBalance(newBalanceBeneficiary);

        bankAccountRepository.save(currentUser.getBankAccount());
        bankAccountRepository.save(beneficiaryUser.getBankAccount());

        Transaction transaction = new Transaction();
        transaction.setBankAccount(currentUser.getBankAccount());
        transaction.setAmount(amountTransaction);
        transaction.setReceiverBankAccount(beneficiaryUser.getBankAccount());
        transaction.setDescription(performTransactionDto.getDescription());
        transaction.setBeneficiaryUsername(beneficiaryUser.getUsername());
        transactionRepository.save(transaction);

    }

    private static BigDecimal addOperation(User beneficiaryUser, BigDecimal amountTransaction) {
        return beneficiaryUser.getBankAccount().getBalance().add(amountTransaction);
    }

    private static BigDecimal subtractOperation(User currentUser, BigDecimal amountTransaction) {
        return currentUser.getBankAccount().getBalance().subtract(amountTransaction);
    }

    public List<Transaction> getTransactionsForUser(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email " + email + " non trouvé"));

        return transactionRepository.findByBankAccountUserEmail(email);
    }

}



