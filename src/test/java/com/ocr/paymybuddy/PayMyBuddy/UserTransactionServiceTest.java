package com.ocr.paymybuddy.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ocr.paymybuddy.PayMyBuddy.exceptions.TransactionException;
import com.ocr.paymybuddy.PayMyBuddy.models.Transaction;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.repositories.BankAccountRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.TransactionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.TransactionService;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.PerformTransactionDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class UserTransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPerformTransaction_Success() throws TransactionException {
        User sender = new User();
        sender.setEmail("sender@example.com");
        sender.setBankAccount(new com.ocr.paymybuddy.PayMyBuddy.models.BankAccount());
        sender.getBankAccount().setBalance(BigDecimal.valueOf(100));

        User recipient = new User();
        recipient.setEmail("recipient@example.com");
        recipient.setBankAccount(new com.ocr.paymybuddy.PayMyBuddy.models.BankAccount());
        recipient.getBankAccount().setBalance(BigDecimal.valueOf(50));

        PerformTransactionDto dto = new PerformTransactionDto("recipient@example.com", BigDecimal.valueOf(10), "Test transaction");

        when(userRepository.findUserByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findUserByEmail("recipient@example.com")).thenReturn(Optional.of(recipient));

        assertDoesNotThrow(() -> transactionService.performTransaction(dto, "sender@example.com"));
    }

    @Test
    void testPerformTransaction_InsufficientFunds() {

        User currentUser = new User();
        currentUser.setEmail("currentUser@example.com");
        currentUser.setBankAccount(new com.ocr.paymybuddy.PayMyBuddy.models.BankAccount());
        currentUser.getBankAccount().setBalance(BigDecimal.valueOf(5));

        User beneficiaryUser = new User();
        beneficiaryUser.setEmail("beneficiaryUser@example.com");
        beneficiaryUser.setBankAccount(new com.ocr.paymybuddy.PayMyBuddy.models.BankAccount());

        PerformTransactionDto dto = new PerformTransactionDto("beneficiaryUser@example.com", BigDecimal.valueOf(10), "Test transaction");

        when(userRepository.findUserByEmail("currentUser@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findUserByEmail("beneficiaryUser@example.com")).thenReturn(Optional.of(beneficiaryUser));

        assertThrows(TransactionException.class, () -> transactionService.performTransaction(dto, "currentUser@example.com"));
    }


    @Test
    void testGetTransactionsForUser_Success() {
        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(transactionRepository.findByBankAccountUserEmail("user@example.com")).thenReturn(List.of(new Transaction(), new Transaction()));

        List<Transaction> transactions = transactionService.getTransactionsForUser("user@example.com");
        assertEquals(2, transactions.size());
    }

    @Test
    void testGetTransactionsForUser_UserNotFound() {
        when(userRepository.findUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transactionService.getTransactionsForUser("notfound@example.com"));
    }
}

