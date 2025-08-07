package com.example.pnmbackend.repository;
import com.example.pnmbackend.model.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,String> {

    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByNome(String nome);
}

