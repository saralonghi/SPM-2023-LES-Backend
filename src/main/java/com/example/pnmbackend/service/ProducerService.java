package com.example.pnmbackend.service;

import com.example.pnmbackend.jwt.JwtService;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.entities.TokenRecovery;
import com.example.pnmbackend.model.type.Role;
import com.example.pnmbackend.model.type.StatusProducer;
import com.example.pnmbackend.repository.NewsLetterRepository;
import com.example.pnmbackend.repository.ProducerRepository;
import com.example.pnmbackend.repository.RecoveryTokenRepository;
import com.example.pnmbackend.request.ChangePasswordRequest;
import com.example.pnmbackend.request.LoginRequest;
import com.example.pnmbackend.request.SignupRequest;
import com.example.pnmbackend.request.UpdateProducerRequest;
import com.example.pnmbackend.response.JwtAuthenticationResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ProducerService{

    @Autowired
    private  EmailService emailService;

    @Autowired
    private NewsLetterRepository newsLetterRepository;


    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private  PasswordEncoder passwordEncoder;


    public Producer getLoggedProducer(String email) {
        return producerRepository.findByEmail(email).orElse(null);

    }
    public void registerProducer(SignupRequest registerRequest) {

        validateRegistration(registerRequest);

        Producer producer = new Producer();
        producer.setID(createID());
        producer.setCreated(LocalDateTime.now());
        producer.setNome(registerRequest.getNome());
        producer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        producer.setFiscalId(registerRequest.getFiscalid());
        producer.setEmail(registerRequest.getEmail());
        producer.setAddress(registerRequest.getAddress());
        producer.setCity(registerRequest.getCity());
        producer.setProvince(registerRequest.getProvince());
        producer.setPhone(registerRequest.getPhone());
        producer.setPostalCode(registerRequest.getPostalCode());
        producer.setStatus(StatusProducer.PENDING.name());
        producer.setRole(Role.PRODUCER);
        producerRepository.save(producer);
    }


    public JwtAuthenticationResponse signIn(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Producer user = producerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        if(user.getStatus().equals(StatusProducer.PENDING.name())){
            throw new IllegalArgumentException("Account non ancora attivato");
        }
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }



    public JwtAuthenticationResponse updateProducerContact(UpdateProducerRequest updateContactRequest) {
        Producer producer = producerRepository.findById(updateContactRequest.getId()).orElseThrow();
        validateContactUpdate(updateContactRequest);
        if(updateContactRequest.getEmail()!=null)
            producer.setEmail(updateContactRequest.getEmail());
        if(updateContactRequest.getPhone()!=null)
            producer.setPhone(updateContactRequest.getPhone());
        if(updateContactRequest.getDescription()!=null)
            producer.setDescription(updateContactRequest.getDescription());
        if(updateContactRequest.getProducts()!=null)
            producer.setProducts(updateContactRequest.getProducts());
        if(updateContactRequest.getAddress()!=null)
            producer.setAddress(updateContactRequest.getAddress());
        if(updateContactRequest.getCity()!=null)
            producer.setCity(updateContactRequest.getCity());
        if(updateContactRequest.getPostalCode()!=null)
            producer.setPostalCode(updateContactRequest.getPostalCode());
        if(updateContactRequest.getProvince()!=null)
            producer.setProvince(updateContactRequest.getProvince());
        if(updateContactRequest.getLinkFacebook()!=null)
            producer.setLinkFacebook(updateContactRequest.getLinkFacebook());
        if(updateContactRequest.getLinkInstagram()!=null)
            producer.setLinkInstagram(updateContactRequest.getLinkInstagram());
        if(updateContactRequest.getLinkWebsite()!=null)
            producer.setLinkWebsite(updateContactRequest.getLinkWebsite());

        producerRepository.save(producer);
        String jwt = jwtService.generateToken(producer);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }



    public void uploadProducerLogo(MultipartFile img) throws IOException {
        Producer producer = getLoggedProducer(SecurityContextHolder.getContext().getAuthentication().getName());
        imageService.saveImageProducer(img, producer);
    }

    public void uploadProducerImages(MultipartFile... img) throws IOException {
        Producer producer = getLoggedProducer(SecurityContextHolder.getContext().getAuthentication().getName());
        for(int i=0; i<img.length; i++){
            imageService.saveImageProducer(img[i], producer);
        }
    }

    public void uploadProducerCover(MultipartFile img) throws IOException {
        Producer producer = getLoggedProducer(SecurityContextHolder.getContext().getAuthentication().getName());
        imageService.saveCover(img, producer);
    }

    private void validateContactUpdate(UpdateProducerRequest updateContactRequest) {
        if(updateContactRequest.getEmail()!=null)
            validateProperty("email", updateContactRequest.getEmail(), "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", "Invalid email address");
        if(updateContactRequest.getPhone()!= null)
            validateProperty("phone", updateContactRequest.getPhone(), "^\\d{10}$", "Invalid phone number format");
    }


    private void validateRegistration(SignupRequest registerRequest) {
        validateProperty("name", registerRequest.getNome(), "^[A-Za-z -]+$", "Invalid name format");
        validateProperty("password", registerRequest.getPassword(), ".{8,}", "Password must be at least 8 characters long");
        validateProperty("fiscalid", registerRequest.getFiscalid(), "^[A-Za-z0-9]+$", "Invalid fiscal ID format");
        validateProperty("email", registerRequest.getEmail(), "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", "Invalid email address");
        validateProperty("address", registerRequest.getAddress(), null, "Address cannot be empty");
        validateProperty("city", registerRequest.getCity(), null, "City cannot be empty");
        validateProperty("phone", registerRequest.getPhone(), "^\\d{10}$", "Invalid phone number format");
        validateProperty("province", registerRequest.getProvince(), null, "Province cannot be empty");
        validateEmail(registerRequest.getEmail());
    }


    private void validateProperty(String propertyName, String propertyValue, String regexPattern, String errorMessage) {
        if (propertyValue == null || (regexPattern != null && !Pattern.matches(regexPattern, propertyValue))) {
            throw new IllegalArgumentException(propertyName + ": " + errorMessage);
        }
    }
    private void validateEmail(String email) {
        if (producerRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email già presente nei nostri sistemi");
        }
    }
    String createID(){
        return UUID.randomUUID().toString();
    }


    public void createRecoverRequest(String email) throws MessagingException {
        Producer producer = producerRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Email non presente nei nostri sistemi"));
        TokenRecovery token = createRecoverToken(producer);
        emailService.sendPasswordResetEmail(token);

    }

    private TokenRecovery createRecoverToken(Producer producer) {
        TokenRecovery token = new TokenRecovery();
        token.setID(UUID.randomUUID().toString());
        token.setEmail(producer.getEmail());
        token.setCreation(LocalDateTime.now());
        recoveryTokenRepository.save(token);
        return token;
    }

    public void validateAndChangePassword(ChangePasswordRequest changePasswordRequest) {
        TokenRecovery tokenRecovery = recoveryTokenRepository.findById(changePasswordRequest.getToken()).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if(calculateTimeDifferenceInMinutes(tokenRecovery.getCreation())<180){
            Producer producer = producerRepository.findByEmail(tokenRecovery.getEmail()).orElseThrow();
            producer.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
            producerRepository.save(producer);
            recoveryTokenRepository.delete(tokenRecovery);
        }else{
            throw new IllegalArgumentException("Token scaduto");
        }
    }


    private long calculateTimeDifferenceInMinutes(LocalDateTime creationTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(creationTime, now);
        return duration.toMinutes();
    }

    public void validateToken(String token) {
       TokenRecovery tokenRecovery  = recoveryTokenRepository.findById(token).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
         if(calculateTimeDifferenceInMinutes(tokenRecovery.getCreation())>180) {
             recoveryTokenRepository.delete(tokenRecovery);
             throw new IllegalArgumentException("Token scaduto");
         }
    }

}
