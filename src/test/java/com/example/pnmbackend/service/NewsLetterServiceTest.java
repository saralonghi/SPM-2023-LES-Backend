package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NewsLetterServiceTest {
    @Mock
    private NewsLetterRepository newsLetterRepository;

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private NewsLetterService newsLetterService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Test
    void createNewsLetterSavesNewsLetterWithImageSuccessfully() throws IOException {
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setTitolo("test");
        newsLetter.setContenuto("test");
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        Producer producer = new Producer();
        producer.setID("8b3b3b3b-8b3b-8b3b-8b3b-8b3b3b3b3b3b");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(producerRepository.findByEmail(any())).thenReturn(Optional.of(producer));
        when(newsLetterRepository.save(any())).thenReturn(newsLetter);

        newsLetterService.createNewsLetter(newsLetter, image);

        verify(newsLetterRepository, times(1)).save(any());
    }


    @Test
    void createNewsLetterSavesNewsLetterWithoutImageSuccessfully() throws IOException {
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setTitolo("test");
        newsLetter.setContenuto("test");
        MultipartFile image = new MockMultipartFile("image", "null", "image/jpeg", "".getBytes());
        Producer producer = new Producer();
        producer.setID("8b3b3b3b-8b3b-8b3b-8b3b-8b3b3b3b3b3b");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(producerRepository.findByEmail(any())).thenReturn(Optional.of(producer));
        when(newsLetterRepository.save(any())).thenReturn(newsLetter);

        newsLetterService.createNewsLetter(newsLetter, image);

        verify(newsLetterRepository, times(1)).save(any());
    }
}
