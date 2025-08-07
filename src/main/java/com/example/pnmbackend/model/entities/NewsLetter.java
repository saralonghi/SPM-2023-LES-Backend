package com.example.pnmbackend.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@Table(name = "newsletter", schema = "dbo")
public class NewsLetter {

    @Id
    @Column(name = "ID")
    private String ID;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "producerID")
    private String producerID;

    @Column(name = "titolo")
    private String titolo;

    @Column(name = "contenuto")
    private String contenuto;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "status")
    private String status;





}
