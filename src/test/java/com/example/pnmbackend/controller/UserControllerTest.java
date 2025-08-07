package com.example.pnmbackend.controller;


import com.example.pnmbackend.request.UserRequest;
import com.example.pnmbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserRequest validRequest = createValidUserRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest.toJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"result\":\"User created successfully\"}"));

    }

    @Test
    void testCreateUserFailure() throws Exception {
        UserRequest invalidRequest = createInvalidUserRequest();
        String errorMessage = "Invalid email address";
        doThrow(new IllegalArgumentException(errorMessage)).when(userService).createUser(invalidRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/createUse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest.toJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    private UserRequest createValidUserRequest() {
        UserRequest validRequest = new UserRequest();
        validRequest.setEmail("user@example.com");
        return validRequest;
    }

    private UserRequest createInvalidUserRequest() {
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setEmail("invalid-email");
        return invalidRequest;
    }

}
