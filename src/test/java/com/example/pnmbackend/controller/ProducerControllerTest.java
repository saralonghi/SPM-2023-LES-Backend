package com.example.pnmbackend.controller;


import com.example.pnmbackend.model.entities.Image;
import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.entities.ProducerDetails;
import com.example.pnmbackend.request.ChangePasswordRequest;
import com.example.pnmbackend.request.LoginRequest;
import com.example.pnmbackend.request.SignupRequest;
import com.example.pnmbackend.request.UpdateProducerRequest;
import com.example.pnmbackend.response.JwtAuthenticationResponse;
import com.example.pnmbackend.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProducerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProducerService producerService;

    @Mock
    private UserService userService;

    @Mock
    private AdminService adminService;

    @Mock
    private NewsLetterService newsLetterService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ProducerController producerController;

    private final String mockrequest = "{\"name\":\"John\",\"password\":\"password\",\"fiscalid\":\"12345\",\"email\":\"john@example.com\"," +
            "\"address\":\"123 Main St\",\"city\":\"City\",\"phone\":\"1234567890\",\"province\":\"Province\",\"postalCode\":\"12345\"}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(producerController).build();
    }

    @Test
    void getLoginReturnsJwtAuthenticationResponseWhenSuccessful() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("leocurzi11@icloud.com");
        loginRequest.setPassword("password");

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken("valid-token");

        when(producerService.signIn(any())).thenReturn(jwtAuthenticationResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(jwtAuthenticationResponse), result.getResponse().getContentAsString());
    }

    @Test
    void getLoginReturnsBadRequestWhenProducerServiceFails() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalid-username");
        loginRequest.setPassword("invalid-password");

        when(producerService.signIn(any())).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }



    @Test
    void testRegisterProducerSuccess() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        doNothing().when(producerService).registerProducer(signupRequest);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockrequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"result\":\"Producer registered successfully\"}"));
    }

    @Test
    void testRegisterProducerFailure() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        String errorMessage = "Invalid registration";
        doThrow(new IllegalArgumentException(errorMessage)).when(producerService).registerProducer(signupRequest);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockrequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void checkEmailExistsReturnsEmailSentSuccessfullyWhenSuccessful() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("leocurzi11@icloud.com");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/passwordRecovered/checkEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Email sent successfully\"}", result.getResponse().getContentAsString());
    }

    @Test
    void checkEmailExistsReturnsBadRequestWhenProducerServiceFails() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalid-email");

        doThrow(new IllegalArgumentException()).when(producerService).createRecoverRequest(any());


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/passwordRecovered/checkEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void validateTokenReturnsTokenValidatedSuccessfullyWhenSuccessful() throws Exception {
        String token = "ebc7b3b3-0b7b-4b7b-8b7b-7b7b7b7b7b7b";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/passwordRecovered/" + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Token validated successfully\"}", result.getResponse().getContentAsString());
    }

    @Test
    void validateTokenReturnsBadRequestWhenProducerServiceFails() throws Exception {
        String token = "invalid-token";

        doThrow(new IllegalArgumentException()).when(producerService).validateToken(any());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/passwordRecovered/" + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void changePasswordReturnsPasswordChangedSuccessfullyWhenSuccessful() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setToken("ebc7b3b3-0b7b-4b7b-8b7b-7b7b7b7b7b7b");
        changePasswordRequest.setPassword("new-password");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/passwordRecovered/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(changePasswordRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"Password changed successfully\"}", result.getResponse().getContentAsString());
    }

    @Test
    void changePasswordReturnsBadRequestWhenProducerServiceFails() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setToken("invalid-token");
        changePasswordRequest.setPassword("new-password");

        doThrow(new IllegalArgumentException()).when(producerService).validateAndChangePassword(any());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/passwordRecovered/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(changePasswordRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    //IMAGES---------------------------------------------------------------------------------------------------------------------------

    @Test
    void updateLogoReturnsSuccessWhenSuccessful() throws Exception {
        MockMultipartFile image = new MockMultipartFile("logo", "test.jpg", "image/jpeg", "test image content".getBytes());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/producer/uploadLogo")
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"success\"}", result.getResponse().getContentAsString());
    }

    @Test
    void updateLogoReturnsBadRequestWhenFails() throws Exception {
        MockMultipartFile image = new MockMultipartFile("logo", "test.jpg", "image/jpeg", "test image content".getBytes());

        doThrow(new IOException()).when(producerService).uploadProducerLogo(any());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/producer/uploadLogo")
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void uploadCoverReturnsSuccessWhenSuccessful() throws Exception {
        MockMultipartFile image = new MockMultipartFile("cover", "test.jpg", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(multipart("/api/producer/uploadCover")
                        .file(image))
                .andExpect(status().isOk());

        verify(producerService, times(1)).uploadProducerCover(image);
    }

    @Test
    void uploadCoverReturnsBadRequestWhenFails() throws Exception {
        MockMultipartFile image = new MockMultipartFile("cover", "test.jpg", "image/jpeg", "test image content".getBytes());

        doThrow(new IOException()).when(producerService).uploadProducerCover(image);

        mockMvc.perform(multipart("/api/producer/uploadCover")
                        .file(image))
                .andExpect(status().isBadRequest());

        verify(producerService, times(1)).uploadProducerCover(image);
    }

    @Test
    void uploadImageReturnsSuccessWhenSuccessful() throws Exception {
        MockMultipartFile image = new MockMultipartFile("other", "test.jpg", "image/jpeg", "test image content".getBytes());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/producer/uploadImages")
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"result\":\"success\"}", result.getResponse().getContentAsString());
    }

    @Test
    void uploadImageReturnsBadRequestWhenFails() throws Exception {
        MockMultipartFile image = new MockMultipartFile("other", "test.jpg", "image/jpeg", "test image content".getBytes());

        doThrow(new IOException()).when(producerService).uploadProducerImages(any());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/producer/uploadImages")
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void deleteAllImagesReturnsSuccessWhenSuccessful() throws Exception {
        String id = "ebc7b3b3-0b7b-4b7b-8b7b-7b7b7b7b7b7b";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/producer/deleteAllImages/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Images deleted successfully", result.getResponse().getContentAsString());
    }


    @Test
    void deleteImageReturnsSuccessWhenSuccessful() throws Exception {
        String id = "ebc7b3b3-0b7b-4b7b-8b7b-7b7b7b7b7b7b";

        doNothing().when(imageService).deleteImage(any());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/producer/deleteImage/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Image deleted successfully", result.getResponse().getContentAsString());
    }


    @Test
    void retrieveLogoReturnsImageWhenSuccessful() throws Exception {
        String id = "ebc7b3b3-0b7b-4b7b-8b7b-7b7b7b7b7b7b";
        Image image = new Image();

        when(imageService.retrieveLogo(any())).thenReturn(image);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/retrieveLogo/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(image), result.getResponse().getContentAsString());
    }

    @Test
    void retrieveLogoReturnsBadRequestWhenFails() throws Exception {
        String id = "eBc7b3b3-0b7b-4b7b-8b7b-7b7b7b7b7b7b";

        when(imageService.retrieveLogo(any())).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/retrieveLogo/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void retrieveAllImagesReturnsImagesWhenSuccessful() throws Exception {
        String id = "ebc7b3b3-0b7b-4b7b-8b7b-7b7b7b7b7b7b";
        List<Image> images = new ArrayList<>();
        images.add(new Image());

        when(imageService.retrieveAllImages(any())).thenReturn(images);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/retrieveAllImages/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(images), result.getResponse().getContentAsString());
    }

    @Test
    void retrieveAllImagesReturnsBadRequestWhenFails() throws Exception {
        String id = "eBc7b3b3-0b7b-4b7b-8b7b-7b7b7b7b7b7b";

        when(imageService.retrieveAllImages(any())).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/retrieveAllImages/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    //PRODUCERS------------------------------------------------------------------------------------------------

    @Test
    void allDetailsProducersReturnsProducerDetailsWhenSuccessful() throws Exception {
        List<Producer> producers = new ArrayList<>();
        List<ProducerDetails> producerDetails = new ArrayList<>();

        when(adminService.retrieveAllProducersApproved()).thenReturn(producers);
        when(userService.createListProducerDetails(producers)).thenReturn(producerDetails);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/producersDetails/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(producerDetails), result.getResponse().getContentAsString());
    }

    @Test
    void getProducerReturnsProducerDetailsWhenSuccessful() throws Exception {
        String id = "ebe89612-02e7-4e23-92ba-94289f0e0b44";
        ProducerDetails producerDetails = new ProducerDetails();

        when(adminService.retrieveProducer(id)).thenReturn(new Producer());
        when(userService.createDetails(any())).thenReturn(producerDetails);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/producersDetails/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(producerDetails), result.getResponse().getContentAsString());
    }

    @Test
    void getProducerReturnsBadRequestWhenAdminServiceFails() throws Exception {
        String id = "invalid-id";

        when(adminService.retrieveProducer(id)).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/producersDetails/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void updateProducerContactReturnsJwtAuthenticationResponseWhenSuccessful() throws Exception {
        UpdateProducerRequest updateContactRequest = new UpdateProducerRequest();
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        when(producerService.updateProducerContact(updateContactRequest)).thenReturn(jwtAuthenticationResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/updateProducer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateContactRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void updateProducerContactReturnsBadRequestWhenProducerServiceFails() throws Exception {
        UpdateProducerRequest updateContactRequest = new UpdateProducerRequest();

        doThrow(new IllegalArgumentException("invalid email")).when(producerService).updateProducerContact(any());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/producer/updateProducer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateContactRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void getProducersReturnsProducerDetailsWhenSuccessful() throws Exception {
        String products = "pasta";
        String province = "Macerata";
        List<ProducerDetails> producerDetails = new ArrayList<>();

        when(userService.getProducersFilter(products, province)).thenReturn(producerDetails);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/getProducers/filtered")
                        .param("products", products)
                        .param("province", province)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(producerDetails), result.getResponse().getContentAsString());
    }

    @Test
    void getProducersReturnsBadRequestWhenUserServiceFails() throws Exception {
        String products = "invalid-products";
        String province = "invalid-province";

        when(userService.getProducersFilter(products, province)).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/producer/getProducers/filtered")
                        .param("products", products)
                        .param("province", province)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }


    //NEWSLETTER------------------------------------------------------------------------------------------------------------------


    @Test
    void createNewsLetterReturnsBadRequestWhenFails() throws Exception {
        NewsLetter newsletter = new NewsLetter();
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());

        doThrow(new IOException()).when(newsLetterService).createNewsLetter(any(), any());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/producer/createNewsLetter")
                        .part(new MockPart("newsletter", new ObjectMapper().writeValueAsBytes(newsletter)))
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(415, result.getResponse().getStatus());
    }
}