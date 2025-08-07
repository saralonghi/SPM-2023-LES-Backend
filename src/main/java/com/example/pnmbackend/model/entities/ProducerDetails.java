package com.example.pnmbackend.model.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class ProducerDetails {

        private String ID;

        private String nome;

        private String email;

        private String province;

        private String city;

        private String address;

        private String phone;

        private String products;

        private String description;

        @Lob
        private byte[] coverPhoto;

        private String linkFacebook;

        private String linkInstagram;

        private String linkWebsite;

        private String urlPhoto;

    }
