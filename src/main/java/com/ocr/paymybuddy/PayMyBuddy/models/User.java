package com.ocr.paymybuddy.PayMyBuddy.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Builder
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

    private String role;

    @OneToOne
    @JoinColumn(name="bankAccount_id")
    private BankAccount bankAccount;

    @OneToMany(mappedBy="user")
    private List<UserConnection> connections;

}
