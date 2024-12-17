package com.ocr.paymybuddy.PayMyBuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "my_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Column(
            unique = true,
            nullable = false
    )
    private String email;

    private String password;

    @OneToOne
    private BankAccount bankAccount;

    @OneToMany
    private List<UserConnection> connections;


}
