package com.example.pnmbackend.repository;

import com.example.pnmbackend.model.entities.TokenRecovery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryTokenRepository extends JpaRepository<TokenRecovery,String> {
    TokenRecovery findByEmail(String email);
    TokenRecovery findByID(String id);
}
