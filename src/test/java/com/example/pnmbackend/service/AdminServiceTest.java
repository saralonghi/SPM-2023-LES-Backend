package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.type.StatusNewsLetter;
import com.example.pnmbackend.model.type.StatusProducer;
import com.example.pnmbackend.repository.AdminRepository;
import com.example.pnmbackend.repository.NewsLetterRepository;
import com.example.pnmbackend.repository.ProducerRepository;
import com.example.pnmbackend.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NewsLetterRepository newsLetterRepository;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateAdminSuccess() {

        SignupRequest validRequest = createValidSignupRequest();


        adminService.createAdmin(validRequest);


        verify(adminRepository, times(1)).save(any());
    }

    @Test
    void testCreateAdminFailure() {

        SignupRequest invalidRequest = createInvalidSignupRequest();

        assertThrows(IllegalArgumentException.class, () -> adminService.createAdmin(invalidRequest));

        verify(adminRepository, times(0)).save(any());
    }

    @Test
    void testRetrieveAllProducersEmptyList() {
        when(producerRepository.findAll()).thenReturn(Collections.emptyList());
        List<Producer> result = adminService.retrieveAllProducersPending();

        assertEquals(0, result.size());
    }


    @Test
    void testRetrieveAllProducersNotEmptyList() {
        List<Producer> producers = Collections.singletonList(new Producer());
        when(producerRepository.findByStatus(StatusProducer.PENDING.name())).thenReturn(producers);

        List<Producer> result = adminService.retrieveAllProducersPending();

        assertEquals(producers.size(), result.size());
    }

    @Test
    void activateProducerWithValidId() {
        String validId = "ebe89612-02e7-4e23-92ba-94289f0e0b44";
        Producer producer = new Producer();
        producer.setStatus(StatusProducer.PENDING.name());

        when(producerRepository.findById(any())).thenReturn(Optional.of(producer));

        adminService.activateProducer(validId);

        assertEquals(StatusProducer.APPROVED.name(), producer.getStatus());
        verify(producerRepository, times(1)).save(producer);
        verify(emailService, times(1)).sendWelcomeEmailToProducer(producer);
    }

    @Test
    void activateProducerWithInvalidId() {
        String invalidId = "invalid-id";

        when(producerRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adminService.activateProducer(invalidId));

        verify(producerRepository, times(0)).save(any());
        verify(emailService, times(0)).sendWelcomeEmailToProducer(any());
    }

    @Test
    void rejectedProducerWithValidId() {
        String validId = "ebe89612-02e7-4e23-92ba-94289f0e0b44";
        Producer producer = new Producer();
        producer.setStatus(StatusProducer.APPROVED.name());

        when(producerRepository.findById(any())).thenReturn(Optional.of(producer));

        adminService.rejectedProducer(validId);

        assertEquals(StatusProducer.REJECTED.name(), producer.getStatus());
        verify(producerRepository, times(1)).save(producer);
    }

    @Test
    void rejectedProducerWithInvalidId() {
        String invalidId = "invalid-id";

        when(producerRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adminService.rejectedProducer(invalidId));

        verify(producerRepository, times(0)).save(any());
    }

    @Test
    void restoreProducerWithValidId() {
        String validId = "ebe89612-02e7-4e23-92ba-94289f0e0b44";
        Producer producer = new Producer();
        producer.setStatus(StatusProducer.REJECTED.name());

        when(producerRepository.findById(any())).thenReturn(Optional.of(producer));

        adminService.restoreProducer(validId);

        assertEquals(StatusProducer.PENDING.name(), producer.getStatus());
        verify(producerRepository, times(1)).save(producer);
    }

    @Test
    void restoreProducerWithInvalidId() {
        String invalidId = "invalid-id";

        when(producerRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adminService.restoreProducer(invalidId));

        verify(producerRepository, times(0)).save(any());
    }

    @Test
    void retrieveAllProducersActiveReturnsOnlyActiveProducers() {
        Producer approvedProducer = new Producer();
        approvedProducer.setNome("Jane Doe");
        approvedProducer.setStatus(StatusProducer.APPROVED.name());

        Producer rejectedProducer = new Producer();
        rejectedProducer.setNome("John Doe");
        rejectedProducer.setStatus(StatusProducer.REJECTED.name());

        Producer pendingProducer = new Producer();
        pendingProducer.setNome("Jack Doe");
        pendingProducer.setStatus(StatusProducer.PENDING.name());

        when(producerRepository.findAll()).thenReturn(Arrays.asList(approvedProducer, rejectedProducer, pendingProducer));

        List<Producer> activeProducers = adminService.retrieveAllProducersActive();

        assertEquals(2, activeProducers.size());
        assertTrue(activeProducers.contains(approvedProducer));
        assertTrue(activeProducers.contains(rejectedProducer));
    }

    @Test
    void retrieveAllProducersActiveReturnsEmptyListWhenNoActiveProducers() {
        when(producerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Producer> activeProducers = adminService.retrieveAllProducersActive();

        assertTrue(activeProducers.isEmpty());
    }

    @Test
    void approveNewsletterWithValidId() {
        String validId = "b1ea6949-403b-4b6b-925d-e137998c53ed";
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setStatus(StatusNewsLetter.PENDING.name());

        when(newsLetterRepository.findById(any())).thenReturn(Optional.of(newsLetter));

        adminService.approveNewsletter(validId);

        assertEquals(StatusNewsLetter.APPROVED.name(), newsLetter.getStatus());
        verify(newsLetterRepository, times(1)).save(newsLetter);
    }

    @Test
    void approveNewsletterWithInvalidId() {
        String invalidId = "invalid-id";

        when(newsLetterRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adminService.approveNewsletter(invalidId));

        verify(newsLetterRepository, times(0)).save(any());
        verify(emailService, times(0)).sendNewsletterToAllUsers(any());
    }

    @Test
    void rejectNewsletterWithValidId() {
        String validId = "b1ea6949-403b-4b6b-925d-e137998c53ed";
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setStatus(StatusNewsLetter.APPROVED.name());

        when(newsLetterRepository.findById(any())).thenReturn(Optional.of(newsLetter));

        adminService.rejectNewsletter(validId);

        assertEquals(StatusNewsLetter.REJECTED.name(), newsLetter.getStatus());
        verify(newsLetterRepository, times(1)).save(newsLetter);
    }

    @Test
    void rejectNewsletterWithInvalidId() {
        String invalidId = "invalid-id";

        when(newsLetterRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adminService.rejectNewsletter(invalidId));

        verify(newsLetterRepository, times(0)).save(any());
    }

    @Test
    void retrieveAllNewsletterActiveReturnsOnlyActiveNewsletters() {
        NewsLetter approvedNewsLetter = new NewsLetter();
        approvedNewsLetter.setStatus(StatusNewsLetter.APPROVED.name());

        NewsLetter rejectedNewsLetter = new NewsLetter();
        rejectedNewsLetter.setStatus(StatusNewsLetter.REJECTED.name());

        NewsLetter pendingNewsLetter = new NewsLetter();
        pendingNewsLetter.setStatus(StatusNewsLetter.PENDING.name());

        when(newsLetterRepository.findAll()).thenReturn(Arrays.asList(approvedNewsLetter, rejectedNewsLetter, pendingNewsLetter));

        List<NewsLetter> activeNewsLetters = adminService.retrieveAllNewsletterActive();

        assertEquals(2, activeNewsLetters.size());
        assertTrue(activeNewsLetters.contains(approvedNewsLetter));
        assertTrue(activeNewsLetters.contains(rejectedNewsLetter));
    }

    @Test
    void retrieveAllNewsletterActiveReturnsEmptyListWhenNoActiveNewsletters() {
        when(newsLetterRepository.findAll()).thenReturn(Collections.emptyList());

        List<NewsLetter> activeNewsLetters = adminService.retrieveAllNewsletterActive();

        assertTrue(activeNewsLetters.isEmpty());
    }
    private SignupRequest createValidSignupRequest() {

        SignupRequest validRequest = new SignupRequest();
        validRequest.setNome("John Doe");
        validRequest.setEmail("john.doe@example.com");
        validRequest.setPassword("password123");

        return validRequest;
    }

    private SignupRequest createInvalidSignupRequest() {
        SignupRequest invalidRequest = new SignupRequest();
        invalidRequest.setNome("");// -("")
        invalidRequest.setEmail("john.doe@example.com");
        invalidRequest.setPassword("password123");

        return invalidRequest;
    }
}
