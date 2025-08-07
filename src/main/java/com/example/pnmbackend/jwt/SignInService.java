package com.example.pnmbackend.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface SignInService {
    UserDetailsService userDetailsService();
}
