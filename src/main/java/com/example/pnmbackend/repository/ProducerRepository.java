package com.example.pnmbackend.repository;
import com.example.pnmbackend.model.entities.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer,String> {
    Optional<Producer> findByEmailAndPassword(String email, String Password);

    Optional<Producer> findByEmail(String email);

    List<Producer> findByStatus(String status);

    Producer findByID(String id);
}
