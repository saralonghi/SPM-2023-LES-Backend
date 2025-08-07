package com.example.pnmbackend.service;


import com.example.pnmbackend.model.entities.Subscribe;
import com.example.pnmbackend.repository.SubscribeRepository;
import com.example.pnmbackend.request.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {


    @InjectMocks
    private UserService userService;

    @Mock
    private SubscribeRepository subscribeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() {
        UserRequest validRequest = createValidUserRequest();
        userService.createUser(validRequest);
        verify(subscribeRepository, times(1)).save(any(Subscribe.class));
    }

    @Test
    void testCreateUserFailure() {
        UserRequest invalidRequest = createInvalidUserRequest();

        try {
            userService.createUser(invalidRequest);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("email: Invalid email address", e.getMessage());
        }

        verify(subscribeRepository, times(0)).save(any(Subscribe.class));
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
