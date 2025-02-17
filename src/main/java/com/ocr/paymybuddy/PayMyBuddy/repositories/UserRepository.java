package com.ocr.paymybuddy.PayMyBuddy.repositories;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);

    User findByUsername(String username);

}