package com.example.pnmbackend.repository;


import com.example.pnmbackend.model.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News,String> {



}
