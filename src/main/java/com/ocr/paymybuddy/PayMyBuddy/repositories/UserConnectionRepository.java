package com.ocr.paymybuddy.PayMyBuddy.repositories;

import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer> {

    List<UserConnection> findByFromUserEmail(String email);

}
