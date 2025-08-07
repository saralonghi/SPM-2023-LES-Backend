package com.example.pnmbackend.repository;


import com.example.pnmbackend.model.entities.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface NewsLetterRepository extends JpaRepository<NewsLetter,String> {
    NewsLetter findByID(String ID);
    List<NewsLetter> findByStatus(String name);
    List<NewsLetter> findByProducerID(String ID);
}
