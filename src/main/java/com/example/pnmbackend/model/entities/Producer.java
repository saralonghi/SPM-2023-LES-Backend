package com.example.pnmbackend.model.entities;

import com.example.pnmbackend.model.type.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "producer", schema = "dbo")
public class Producer implements UserDetails , Comparable<Producer>{

        @Id
        private String ID;

        @Column(name = "nome")
        private String nome;

        @Column(name = "email")
        private String email;

        @Column(name = "password")
        private String password;

        @Column(name = "fiscalid")
        private String fiscalId;

        @Column(name = "province")
        private String province;

        @Column(name = "city")
        private String city;

        @Column(name = "postalcode")
        private Integer postalCode;

        @Column(name = "address")
        private String address;

        @Column(name = "phone")
        private String phone;

        @Column(name = "products")
        private  String products;

        @Column(name = "description")
        private String description;

        @Column(name ="link_facebook")
        private String linkFacebook;

        @Column(name ="link_instagram")
        private String linkInstagram;

        @Column(name ="link_website")
        private String linkWebsite;

        @Column(name = "status")
        private String status;

        @Column(name = "role")
        @Enumerated(EnumType.STRING)
        private Role role;

        @Column(name = "created")
        private LocalDateTime created;


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(role.name()));
        }

        @Override
        public String getUsername() {
                return email;
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }


        @Override
        public int compareTo(Producer altraPersona) {
          int r = getNome().compareTo(altraPersona.getNome());
          return r;
        }
}