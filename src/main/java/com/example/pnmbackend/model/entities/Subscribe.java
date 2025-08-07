package com.example.pnmbackend.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "subscribe", schema = "dbo")
public class Subscribe {

    @Id
    private String ID;

    @Column(name = "email")
    private String email;


}
