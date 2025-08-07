package com.example.pnmbackend.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "token_recovery", schema = "dbo")
public class TokenRecovery {

        @Id
        private String ID;

        @Column(name = "email")
        private String email;

        @Column(name = "creation")
        private LocalDateTime creation;

}
