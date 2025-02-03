package com.ocr.paymybuddy.PayMyBuddy.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
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

    @OneToOne(cascade = CascadeType.ALL)
    private BankAccount bankAccount;

    @OneToMany(fetch = FetchType.LAZY)
    private List<UserConnection> connections = new ArrayList<>();

}
