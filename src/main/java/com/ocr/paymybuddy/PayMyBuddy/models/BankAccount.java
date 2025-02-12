package com.ocr.paymybuddy.PayMyBuddy.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal balance;

    private String accountNumber;

    @OneToMany(mappedBy = "bankAccount")
    private List<Transaction> transactions;

    @OneToOne(mappedBy = "bankAccount")
    private User user;

}
