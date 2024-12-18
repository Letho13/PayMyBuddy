package com.ocr.paymybuddy.PayMyBuddy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "connections")
public class UserConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
