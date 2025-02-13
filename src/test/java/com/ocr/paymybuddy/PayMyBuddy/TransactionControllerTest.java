package com.ocr.paymybuddy.PayMyBuddy;

import com.ocr.paymybuddy.PayMyBuddy.controllers.TransactionController;
import com.ocr.paymybuddy.PayMyBuddy.exceptions.TransactionException;
import com.ocr.paymybuddy.PayMyBuddy.mapper.ConnectionMapper;
import com.ocr.paymybuddy.PayMyBuddy.models.Transaction;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserConnectionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.BankAccountService;
import com.ocr.paymybuddy.PayMyBuddy.services.TransactionService;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.ConnectionDto;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.PerformTransactionDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private UserConnectionRepository userConnectionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ConnectionMapper connectionMapper;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private Model model;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void testTransactionPage_Success() {

        User user = new User();
        user.setEmail("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userConnectionRepository.findByFromUserEmail("test@example.com")).thenReturn(Collections.emptyList());
        when(connectionMapper.connectionDtoFromConnection(Collections.emptyList())).thenReturn(Collections.emptyList());
        when(bankAccountService.getBalanceForLoggedInUser()).thenReturn(BigDecimal.TEN);
        when(transactionService.getTransactionsForUser("test@example.com")).thenReturn(Collections.emptyList());

        String viewName = transactionController.transaction(model);

        assertEquals("transaction_page", viewName);
    }

    @Test
    void testTransactionPage_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transactionController.transaction(model));
    }

    @Test
    void testHandleTransaction_Success() throws TransactionException {

        PerformTransactionDto dto = new PerformTransactionDto();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        ResponseEntity<Map<String, String>> response = transactionController.handleTransaction(dto);
        verify(transactionService).performTransaction(dto, "test@example.com");
        assertEquals("success", response.getBody().get("messageType"));
        assertEquals("Transaction réussie !", response.getBody().get("message"));
    }

    @Test
    void testHandleTransaction_Failure() throws TransactionException {
        PerformTransactionDto dto = new PerformTransactionDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        doThrow(new TransactionException("Erreur transaction"))
                .when(transactionService).performTransaction(dto, "test@example.com");

        ResponseEntity<Map<String, String>> response = transactionController.handleTransaction(dto);

        assertEquals("danger", response.getBody().get("messageType"));
        assertEquals("Erreur : Erreur transaction", response.getBody().get("message"));
    }

    @Test
    void testAddAmountToBankAccount_Success() {
        BigDecimal amount = BigDecimal.valueOf(100);

        String viewName = transactionController.addAmountToBankAccount(amount, model);

        verify(bankAccountService).addAmountToConnectedUserBankAccount(amount);
        assertEquals("redirect:/transaction", viewName);
    }

    @Test
    void testAddAmountToBankAccount_Failure() {
        BigDecimal amount = BigDecimal.valueOf(100);
        doThrow(new RuntimeException("Erreur de dépôt"))
                .when(bankAccountService).addAmountToConnectedUserBankAccount(amount);

        String viewName = transactionController.addAmountToBankAccount(amount, model);

        verify(model).addAttribute("errorMessage", "Erreur : Erreur de dépôt");
        assertEquals("redirect:/transaction", viewName);
    }
}
