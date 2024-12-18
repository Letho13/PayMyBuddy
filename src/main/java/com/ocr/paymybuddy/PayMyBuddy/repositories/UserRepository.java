package com.ocr.paymybuddy.PayMyBuddy.repositories;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository <User, Integer> {


}