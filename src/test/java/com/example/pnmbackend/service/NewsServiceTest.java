package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.Admin;
import com.example.pnmbackend.model.entities.Image;
import com.example.pnmbackend.model.entities.News;
import com.example.pnmbackend.repository.AdminRepository;
import com.example.pnmbackend.repository.ImageRepository;
import com.example.pnmbackend.repository.NewsRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewsSavesNewsSuccessfully() throws IOException {
        News news = new News();
        MultipartFile[] images = {new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes())};
        Admin admin = new Admin();
        admin.setID("8b3b3b3b-8b3b-8b3b-8b3b-8b3b3b3b3b3b");

        // Set up the SecurityContext to return the mocked Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Set up the Authentication to return a specific name
        when(authentication.getName()).thenReturn("test@example.com");

        // Set the SecurityContextHolder to return the mocked SecurityContext
        SecurityContextHolder.setContext(securityContext);


        when(adminRepository.findByEmail(any())).thenReturn(Optional.of(admin));
        when(imageRepository.save(any())).thenReturn(new Image());
        when(newsRepository.save(any())).thenReturn(news);

        newsService.createNews(news, images);

        verify(newsRepository, times(1)).save(any());
        verify(imageRepository, times(1)).save(any());
    }


    
    @Test
    void deleteNewsDeletesNewsSuccessfully() {
        String id = "8b3b3b3b-8b3b-8b3b-8b3b-8b3b3b3b3b3b";

        doNothing().when(newsRepository).deleteById(any());

        newsService.deleteNews(id);

        verify(newsRepository, times(1)).deleteById(any());
    }


    @Test
    void retrieveAllNewsReturnsAllNewsSuccessfully() {
        List<News> expectedNewsList = Arrays.asList(new News(), new News());
        when(newsRepository.findAll()).thenReturn(expectedNewsList);

        List<News> actualNewsList = newsService.retrieveAllNews();

        assertEquals(expectedNewsList, actualNewsList);
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void retrieveAllNewsReturnsEmptyListWhenNoNews() {
        List<News> expectedNewsList = new ArrayList<>();
        when(newsRepository.findAll()).thenReturn(expectedNewsList);

        List<News> actualNewsList = newsService.retrieveAllNews();

        assertEquals(expectedNewsList, actualNewsList);
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void retrieveNewsReturnsNewsSuccessfully() {
        String id = "8b3b3b3b-8b3b-8b3b-8b3b-8b3b3b3b3b3b";
        News expectedNews = new News();
        when(newsRepository.findById(any())).thenReturn(Optional.of(expectedNews));

        News actualNews = newsService.retrieveNews(id);

        assertEquals(expectedNews, actualNews);
        verify(newsRepository, times(1)).findById(any());
    }

    @Test
    void retrieveNewsReturnsNullWhenNoNewsFound() {
        String id = "8b3b3b3b-8b3b-8b3b-8b3b-8b3b3b3b3b3b";
        when(newsRepository.findById(any())).thenReturn(Optional.empty());

        News actualNews = newsService.retrieveNews(id);

        assertNull(actualNews);
        verify(newsRepository, times(1)).findById(any());
    }


}
