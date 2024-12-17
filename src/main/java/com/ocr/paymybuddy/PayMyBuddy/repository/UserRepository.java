package com.ocr.paymybuddy.PayMyBuddy.repository;

import com.ocr.paymybuddy.PayMyBuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository <User, Integer> {


}