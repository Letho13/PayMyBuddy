package com.ocr.paymybuddy.PayMyBuddy.services;

import com.ocr.paymybuddy.PayMyBuddy.exceptions.TransactionException;
import com.ocr.paymybuddy.PayMyBuddy.models.Transaction;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import com.ocr.paymybuddy.PayMyBuddy.repositories.BankAccountRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.TransactionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserConnectionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.PerformTransactionDto;
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
    private final UserConnectionRepository userConnectionRepository;


    public List<Transaction> findAll() {

        return transactionRepository.findAll();
    }

    public void addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Transactional(rollbackFor = {TransactionException.class})
    public void performTransaction(PerformTransactionDto performTransactionDto, String emailCurrentUser) throws TransactionException {

        BigDecimal amountTransaction = performTransactionDto.getAmount();

        User currentUser = userRepository.findUserByEmail(emailCurrentUser).get();
        User beneficiaryUser = userRepository.findUserByEmail(performTransactionDto.getEmailBeneficiary()).get();

        BigDecimal balanceCurrentUser = currentUser.getBankAccount().getBalance();
        if (balanceCurrentUser.compareTo(amountTransaction) < 0) {
            throw new TransactionException("Montant insuffisant");
        }


        transactionOperation(currentUser, subtractOperation(currentUser, amountTransaction));

        transactionOperation(beneficiaryUser, addOperation(beneficiaryUser, amountTransaction));

    }


    private static BigDecimal addOperation(User beneficiaryUser, BigDecimal amountTransaction) {
        return beneficiaryUser.getBankAccount().getBalance().add(amountTransaction);
    }

    private static BigDecimal subtractOperation(User currentUser, BigDecimal amountTransaction) {
        return currentUser.getBankAccount().getBalance().subtract(amountTransaction);
    }

    private void transactionOperation(User user, BigDecimal amountTransaction) {
        Transaction transactionCredit = new Transaction();
        transactionCredit.setBankAccount(user.getBankAccount());
        transactionCredit.setAmount(amountTransaction);
        transactionRepository.save(transactionCredit);
    }
}



