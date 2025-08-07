package com.example.pnmbackend.jwt.impl;
import com.example.pnmbackend.jwt.SignInService;
import com.example.pnmbackend.repository.AdminRepository;
import com.example.pnmbackend.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SignInDetailsImpl implements SignInService {

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private AdminRepository adminRepository;


    @Override
    public UserDetailsService userDetailsService() {
        return this::createUserDetails;

    }


    private UserDetails createUserDetails(String username){
        UserDetails userDetails = producerRepository.findByEmail(username).orElse(null);
        return userDetails!=null?userDetails:adminRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
