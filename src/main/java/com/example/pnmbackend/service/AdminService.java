package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.Admin;
import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.type.Role;
import com.example.pnmbackend.model.type.StatusNewsLetter;
import com.example.pnmbackend.model.type.StatusProducer;
import com.example.pnmbackend.jwt.JwtService;
import com.example.pnmbackend.repository.AdminRepository;
import com.example.pnmbackend.repository.NewsLetterRepository;
import com.example.pnmbackend.repository.ProducerRepository;
import com.example.pnmbackend.request.LoginRequest;
import com.example.pnmbackend.request.SignupRequest;
import com.example.pnmbackend.response.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class AdminService  {

    @Autowired
    private NewsLetterRepository newsLetterRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private  EmailService emailService;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public void createAdmin(SignupRequest signupRequest){

        validateCreationAdmin(signupRequest);
        Admin admin = new Admin();
        admin.setID(createID());
        admin.setNome(signupRequest.getNome());
        admin.setEmail(signupRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        admin.setRole(Role.ADMIN);
        adminRepository.save(admin);

    }

    public JwtAuthenticationResponse signIn(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Admin user = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    String createID(){ return UUID.randomUUID().toString(); }
    public Admin getLoggedAdmin(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }


    public void deleteProducer(String id) {
        producerRepository.deleteById(id);
    }
    public void activateProducer(String id) {
        Producer producer = producerRepository.findById(id).orElseThrow();
        producer.setStatus(StatusProducer.APPROVED.name());
        producerRepository.save(producer);
        emailService.sendWelcomeEmailToProducer(producer);
    }
    public void rejectedProducer(String id) {
        Producer producer = producerRepository.findById(id).orElseThrow();
        producer.setStatus(StatusProducer.REJECTED.name());
        producerRepository.save(producer);
    }
    public void restoreProducer(String id) {
        Producer producer = producerRepository.findById(id).orElseThrow();
        producer.setStatus(StatusProducer.PENDING.name());
        producerRepository.save(producer);
    }

   /* public List<Producer> sortListOfProducer (List<Producer> list){
        Collections.sort(list);
        return list;
    }*/

//Producers------------------------------------------------------------------------
    public List<Producer> retrieveAllProducers() {
        return producerRepository.findAll();
    }
    public List<Producer> retrieveAllProducersActive() {
        List<Producer> producers = new ArrayList<>();
        for (Producer producer : retrieveAllProducers()) {
            if(producer.getStatus().equals("REJECTED") || producer.getStatus().equals("APPROVED") ){
                producers.add(producer);
            }
        }
        Collections.sort(producers);
        return producers;
    }

    public List<Producer> retrieveAllProducersPending() {
           List<Producer> list = producerRepository.findByStatus(StatusProducer.PENDING.name());
           Collections.sort(list);
        return list;
    }
    public List<Producer> retrieveAllProducersApproved() {
        return producerRepository.findByStatus(StatusProducer.APPROVED.name());
    }
    public List<Producer> retrieveAllProducersRejected() {
        return producerRepository.findByStatus(StatusProducer.REJECTED.name());
    }
    public Producer retrieveProducer(String id) {
        return producerRepository.findByID(id);
    }

    public String retrieveProducerName(String id) {
        return producerRepository.findByID(id).getNome();
    }

    private void validateCreationAdmin(SignupRequest signupRequest) {
        validateProperty("nome", signupRequest.getNome(), "^[A-Za-z -]+$", "Invalid name format");
        validateProperty("email", signupRequest.getEmail(), "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", "Invalid email address");
        validateProperty("password", signupRequest.getPassword(), ".{8,}", "Password must be at least 8 characters long");
    }
    private void validateProperty(String propertyName, String propertyValue, String regexPattern, String errorMessage) {
        if (propertyValue == null || (regexPattern != null && !Pattern.matches(regexPattern, propertyValue))) {
            throw new IllegalArgumentException(propertyName + ": " + errorMessage);
        }
    }
    public Admin getLoggedProducer(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }


    //Newsletter---------------------------------------------------------------------------------

    public void deleteNewsletter(String newsletterId) {
        Optional<NewsLetter> optionalNewsLetter = newsLetterRepository.findById(newsletterId);
        if (optionalNewsLetter.isPresent()) {
            newsLetterRepository.deleteById(newsletterId);
        } else {
            throw new NoSuchElementException("NewsLetter with ID " + newsletterId + " not found");
        }
    }
    public void restoreNewsletter(String id) {
        NewsLetter newsLetter = newsLetterRepository.findById(id).orElseThrow();
        newsLetter.setStatus(StatusNewsLetter.PENDING.name());
        newsLetterRepository.save(newsLetter);
    }
    public NewsLetter retrieveNewsletter(String id) {
        return newsLetterRepository.findByID(id);
    }

    public void approveNewsletter(String id) {
        NewsLetter nl = newsLetterRepository.findById(id).orElseThrow();
        nl.setStatus(StatusNewsLetter.APPROVED.name());
        newsLetterRepository.save(nl);
    }

    public void sendNewsletter(String id){
        NewsLetter newsletter = newsLetterRepository.findByID(id);
        emailService.sendNewsletterToAllUsers(newsletter);
    }

    public void rejectNewsletter(String id) {
        NewsLetter nl = newsLetterRepository.findById(id).orElseThrow();
        nl.setStatus(StatusNewsLetter.REJECTED.name());
        newsLetterRepository.save(nl);
    }

    public List<NewsLetter> retrieveAllNewsletter() {
        return newsLetterRepository.findAll();
    }


    public List<NewsLetter> retrieveAllNewsletterPending() {
        return newsLetterRepository.findByStatus(StatusNewsLetter.PENDING.name());
    }

    public List<NewsLetter> retrieveAllNewsletterActive() {
        List<NewsLetter> list= new ArrayList<>();
        for (NewsLetter newsletter : retrieveAllNewsletter()) {
            if(!newsletter.getStatus().equals(StatusNewsLetter.PENDING.name())){
                list.add(newsletter);
            }}
        return list;
    }



}
