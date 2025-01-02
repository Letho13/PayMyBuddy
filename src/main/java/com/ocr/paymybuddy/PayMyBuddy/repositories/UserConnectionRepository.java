package com.ocr.paymybuddy.PayMyBuddy.repositories;

import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer> {

}
