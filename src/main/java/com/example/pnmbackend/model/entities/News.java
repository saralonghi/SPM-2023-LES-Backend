package com.example.pnmbackend.model.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Data
@Table(name = "news", schema = "dbo")
public class News implements Comparable<News>{

    @Id
    @Column(name = "ID")
    private String ID;

    @Column(name = "adminID")
    private String adminID;

    @Column(name = "titolo")
    private String titolo;

    @Column(name = "contenuto")
    private String contenuto;

    @Column(name = "created")
    private LocalDateTime created;

    @Override
    public int compareTo(News other) {
        if (this.getCreated() == null || other.getCreated() == null) {
            return 0;
        }
        return other.getCreated().compareTo(this.getCreated());
    }
}
