package com.ocr.paymybuddy.PayMyBuddy.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double amount;

    private String description;

    @ManyToOne
    @JoinColumn(name = "bankAccount_id")
    private BankAccount bankAccount;

}
