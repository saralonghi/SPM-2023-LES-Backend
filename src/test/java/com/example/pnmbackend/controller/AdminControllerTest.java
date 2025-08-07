package com.example.pnmbackend.controller;


import com.example.pnmbackend.model.entities.Admin;
import com.example.pnmbackend.model.entities.News;
import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.type.Role;
import com.example.pnmbackend.request.LoginRequest;
import com.example.pnmbackend.request.SignupRequest;
import com.example.pnmbackend.response.JwtAuthenticationResponse;
import com.example.pnmbackend.service.AdminService;
import com.example.pnmbackend.service.NewsLetterService;
import com.example.pnmbackend.service.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @Mock
    private NewsService newsService;

    @InjectMocks
    private AdminController adminController;

    private final String mockrequest = "{\"name\":\"John\",\"password\":\"password\",\"email\":\"john@example.com\"}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

    }


    @Test
    void adminLoginWithValidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("leocurzi1409@gmail.com");
        loginRequest.setPassword("password123");

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken("valid-token");

        when(adminService.signIn(any())).thenReturn(jwtAuthenticationResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(jwtAuthenticationResponse), result.getResponse().getContentAsString());
    }

    @Test
    void adminLoginWithInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalid-email");
        loginRequest.setPassword("invalid-password");

        JwtAuthenticationResponse jwtAuthenticationResponse = JwtAuthenticationResponse.builder().error("Authentication failed").build();

        when(adminService.signIn(any())).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(jwtAuthenticationResponse), result.getResponse().getContentAsString());
    }
        @Test
        void testCreateAdminSuccess() throws Exception {

            SignupRequest signupRequest = new SignupRequest();
            doNothing().when(adminService).createAdmin(signupRequest);


            mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mockrequest)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("{\"result\":\"Admin registered successfully\"}"));//added \"result"\:
        }

        @Test
        void testCreateAdminFailure() throws Exception {

            SignupRequest signupRequest = new SignupRequest();
            String errorMessage = "Invalid registration";
            doThrow(new IllegalArgumentException(errorMessage)).when(adminService).createAdmin(signupRequest);


            mockMvc.perform(MockMvcRequestBuilders.post("/api/error")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mockrequest)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    @Test
    void getProducerReturnsProducerWhenExists() throws Exception {
        Producer producer = new Producer();
        producer.setID("ebe89612-02e7-4e23-92ba-94289f0e0b44");
        producer.setRole(Role.PRODUCER);
        when(adminService.retrieveProducer(any())).thenReturn(producer);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/producer/ebe89612-02e7-4e23-92ba-94289f0e0b44")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(producer), result.getResponse().getContentAsString());
    }

    @Test
    void getProducerReturnsBadRequestWhenDoesNotExist() throws Exception {
        when(adminService.retrieveProducer(any())).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/producer/invalid-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void approveProducerReturnsValidRegistrationWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/approveProducer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Admin registered successfully\"}", result.getResponse().getContentAsString());
    }

    @Test
    void rejectProducerReturnsValidRejectedWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/rejectProducer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Producer rejected successfully\"}", result.getResponse().getContentAsString());
    }

    @Test
    void restoreProducerReturnsProducerRestoredWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/restoreProducer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Producer restored\"}", result.getResponse().getContentAsString());
    }

    @Test
    void deleteProducerReturnsAccountDeletedSuccessfullyWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/producerDelete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Account deleted successfully", result.getResponse().getContentAsString());
    }

    @Test
    void allProducersNotActiveReturnsEmptyListWhenNoProducers() throws Exception {
        when(adminService.retrieveAllProducersPending()).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/allProducer/notActive")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    void allProducersActiveReturnsProducersWhenExist() throws Exception {
        Producer producer = new Producer();
        producer.setID("ebe89612-02e7-4e23-92ba-94289f0e0b44");
        producer.setRole(Role.PRODUCER);

        when(adminService.retrieveAllProducersActive()).thenReturn(Collections.singletonList(producer));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/allProducer/active")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(Collections.singletonList(producer)), result.getResponse().getContentAsString());
    }

    @Test
    void allProducersActiveReturnsEmptyListWhenNoProducers() throws Exception {
        when(adminService.retrieveAllProducersActive()).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/allProducer/active")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    // News ----------------------------------------------------------------------------------------------



    @Test
    void deleteNewsReturnsNewsDeletedSuccessfullyWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/deleteNews/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"News deleted successfully\"}", result.getResponse().getContentAsString());
    }

    @Test
    void allNewsReturnsNewsListWhenExist() throws Exception {
        News news = new News();
        news.setTitolo("valid-title");

        when(newsService.retrieveAllNews()).thenReturn(Collections.singletonList(news));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/allNews")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(Collections.singletonList(news)), result.getResponse().getContentAsString());
    }

    @Test
    void getNewsReturnsNewsWhenExists() throws Exception {
        News news = new News();
        news.setTitolo("valid-title");

        when(newsService.retrieveNews(any())).thenReturn(news);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/news/valid-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(news), result.getResponse().getContentAsString());
    }

    @Test
    void getNewsReturnsBadRequestWhenDoesNotExist() throws Exception {
        when(newsService.retrieveNews(any())).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/news/invalid-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    // Newsletter -----------------------------------------------------------------------------------------

    @Test
    void allNewsletterNotActiveReturnsNewslettersWhenExist() throws Exception {
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setID("ebe89612-02e7-4e23-92ba-94289f0e0b44");

        when(adminService.retrieveAllNewsletterPending()).thenReturn(Collections.singletonList(newsLetter));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/allNewsletter/notActive")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(Collections.singletonList(newsLetter)), result.getResponse().getContentAsString());
    }

    @Test
    void allNewsletterNotActiveReturnsEmptyListWhenNoNewsletters() throws Exception {
        when(adminService.retrieveAllNewsletterPending()).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/allNewsletter/notActive")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    void allNewsletterActiveReturnsNewslettersWhenExist() throws Exception {
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setID("ebe89612-02e7-4e23-92ba-94289f0e0b44");

        when(adminService.retrieveAllNewsletterActive()).thenReturn(Collections.singletonList(newsLetter));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/allNewsletter/active")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(Collections.singletonList(newsLetter)), result.getResponse().getContentAsString());
    }

    @Test
    void allNewsletterActiveReturnsEmptyListWhenNoNewsletters() throws Exception {
        when(adminService.retrieveAllNewsletterActive()).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/allNewsletter/active")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    void getNewsletterReturnsNewsletterWhenExists() throws Exception {
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setID("ebe89612-02e7-4e23-92ba-94289f0e0b44");

        when(adminService.retrieveNewsletter(any())).thenReturn(newsLetter);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/newsletter/valid-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(newsLetter), result.getResponse().getContentAsString());
    }

    @Test
    void getNewsletterReturnsBadRequestWhenDoesNotExist() throws Exception {
        when(adminService.retrieveNewsletter(any())).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/newsletter/invalid-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void approveNewsletterReturnsApprovedSuccessfullyWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/approveNewsletter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Newsletter approved successfully\"}", result.getResponse().getContentAsString());
    }

    @Test
    void rejectNewsletterReturnsRejectedSuccessfullyWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/rejectNewsletter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Validation failed\"}", result.getResponse().getContentAsString());
    }

    @Test
    void restoreNewsletterReturnsRestoredSuccessfullyWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/restoreNewsletter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Newsletter restored successfully\"}", result.getResponse().getContentAsString());
    }

    @Test
    void deleteNewsletterReturnsDeletedSuccessfullyWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/deleteNewsletter/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Newsletter deleted successfully\"}", result.getResponse().getContentAsString());
    }
}

