package com.ocr.paymybuddy.PayMyBuddy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "connections",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "targeted_user_id"})
)
public class UserConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;


    @JoinColumn(name = "targeted_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User toTargeted;

}
