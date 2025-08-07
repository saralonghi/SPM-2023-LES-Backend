package com.example.pnmbackend.model.entities;

import com.example.pnmbackend.model.type.TypeImage;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "image", schema = "dbo")
public class Image {

    @Id
    @Column(name = "ID")
    private String ID;

    @Column(name = "IDOwner")
    private String IDOwner;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeImage type;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "data_image")
    private byte[] dataImage;
}
