package com.ocr.paymybuddy.PayMyBuddy.repositories;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer> {

    // Trouver toutes les relations pour un utilisateur donné
    List<UserConnection> findByUsername(User user);

    // Trouver toutes les relations pour un utilisateur donné
    List<UserConnection> findByEmail(User user);


}
