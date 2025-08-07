package com.example.pnmbackend.service;

import com.example.pnmbackend.jwt.JwtService;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.repository.ProducerRepository;
import com.example.pnmbackend.repository.RecoveryTokenRepository;
import com.example.pnmbackend.request.LoginRequest;
import com.example.pnmbackend.request.SignupRequest;
import com.example.pnmbackend.request.UpdateProducerRequest;
import com.example.pnmbackend.response.JwtAuthenticationResponse;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProducerServiceTest {

    @InjectMocks
    private ProducerService producerService;

    @Mock
    private AdminService adminService;

    @Mock
    private EmailService emailService;

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RecoveryTokenRepository recoveryTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateProducerContactSuccess() {
        UpdateProducerRequest validRequest = createValidUpdateProducerRequest();

        when(producerRepository.findById(any())).thenReturn(Optional.of(new Producer()));
        when(jwtService.generateToken(any())).thenReturn("testToken");

        JwtAuthenticationResponse response = producerService.updateProducerContact(validRequest);

        assertEquals("testToken", response.getToken());
        verify(producerRepository, times(1)).save(any());
    }

    @Test
    void updateProducerContactFailure() {
        UpdateProducerRequest invalidRequest = createInvalidUpdateProducerRequest();

        when(producerRepository.findById(any())).thenReturn(Optional.of(new Producer()));
        assertThrows(IllegalArgumentException.class, () -> producerService.updateProducerContact(invalidRequest));

        verify(producerRepository, times(0)).save(any());
    }


    @Test
    void signInWithValidCredentialsReturnsJwt() {
        LoginRequest validRequest = new LoginRequest();
        validRequest.setEmail("john.doe@example.com");
        validRequest.setPassword("password123");

        when(producerRepository.findByEmail(any())).thenReturn(Optional.of(buildProducer()));
        when(jwtService.generateToken(any())).thenReturn("testToken");

        JwtAuthenticationResponse response = producerService.signIn(validRequest);

        assertEquals("testToken", response.getToken());
    }

    @Test
    void signInWithInvalidEmailThrowsException() {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("invalid.email@example.com");
        invalidRequest.setPassword("password123");

        when(producerRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> producerService.signIn(invalidRequest));
    }

    @Test
    void signInWithInvalidPasswordThrowsException() {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("john.doe@example.com");
        invalidRequest.setPassword("invalidPassword");
        assertThrows(IllegalArgumentException.class, () -> producerService.signIn(invalidRequest));
    }


    @Test
    void testRegisterProducerSuccess() {

        SignupRequest validRequest = createValidSignupRequest();


        producerService.registerProducer(validRequest);


        verify(producerRepository, times(1)).save(any());
    }

    @Test
    void testRegisterProducerFailure() {

        SignupRequest invalidRequest = createInvalidSignupRequest();

        assertThrows(IllegalArgumentException.class, () -> producerService.registerProducer(invalidRequest));

        verify(producerRepository, times(0)).save(any());
    }

    @Test
    void createRecoverRequestWithValidEmail() throws MessagingException {
        String validEmail = "john.doe@example.com";

        when(producerRepository.findByEmail(any())).thenReturn(Optional.of(new Producer()));

        producerService.createRecoverRequest(validEmail);

        verify(producerRepository, times(1)).findByEmail(validEmail);
        verify(emailService, times(1)).sendPasswordResetEmail(any());
    }

    @Test
    void createRecoverRequestWithInvalidEmail() throws MessagingException {
        String invalidEmail = "invalid.email@example.com";

        when(producerRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> producerService.createRecoverRequest(invalidEmail));

        verify(producerRepository, times(1)).findByEmail(invalidEmail);
        verify(emailService, times(0)).sendPasswordResetEmail(any());
    }


    private SignupRequest createValidSignupRequest() {

        SignupRequest validRequest = new SignupRequest();
        validRequest.setNome("John Doe");
        validRequest.setPassword("password123");
        validRequest.setFiscalid("ABC123");
        validRequest.setEmail("john.doe@example.com");
        validRequest.setAddress("123 Main St");
        validRequest.setCity("City");
        validRequest.setProvince("Province");
        validRequest.setPhone("1234567890");
        validRequest.setPostalCode(62010);

        return validRequest;
    }

    private SignupRequest createInvalidSignupRequest() {
        SignupRequest invalidRequest = new SignupRequest();
        invalidRequest.setNome("John Doe");
        invalidRequest.setPassword("password123");
        invalidRequest.setEmail("john.doe@example.com");
        invalidRequest.setAddress("123 Main St");
        invalidRequest.setCity("City");
        invalidRequest.setProvince("Province");
        invalidRequest.setPhone("1234567890");
        invalidRequest.setPostalCode(62010);

        return invalidRequest;
    }
    private UpdateProducerRequest createValidUpdateProducerRequest() {
        UpdateProducerRequest validRequest = new UpdateProducerRequest();
        validRequest.setId("123");
        validRequest.setEmail("john.doe@example.com");
        validRequest.setPhone("1234567890");
        validRequest.setDescription("Test description");
        validRequest.setProducts("Test products");

        return validRequest;
    }

    private UpdateProducerRequest createInvalidUpdateProducerRequest() {
        UpdateProducerRequest invalidRequest = new UpdateProducerRequest();
        invalidRequest.setId("123");
        invalidRequest.setEmail("john.doeexample.com");
        invalidRequest.setPhone("1234567890");
        invalidRequest.setDescription("Test description");
        invalidRequest.setProducts("Test products");

        return invalidRequest;
    }

    private Producer buildProducer() {
        Producer producer = new Producer();
        producer.setEmail("prova@prova.it");
        producer.setPassword("password");
        producer.setNome("prova");
        producer.setStatus("ACTIVE");
        return producer;
    }



}


