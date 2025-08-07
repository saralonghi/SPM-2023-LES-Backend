package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.*;
import com.example.pnmbackend.model.type.TypeImage;
import com.example.pnmbackend.repository.ImageRepository;
import com.example.pnmbackend.repository.SubscribeRepository;
import com.example.pnmbackend.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {


    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ImageRepository imageRepository;


    public void createUser(UserRequest userRequest) {
        validateUserCreation(userRequest);

        Subscribe user = new Subscribe();
        user.setID(createID());
        user.setEmail(userRequest.getEmail());
        validateEmail(user.getEmail());
        subscribeRepository.save(user);
    }

    public List<ProducerDetails> getProducersFilter(String products, String province) {
        List<Producer> producers = adminService.retrieveAllProducersApproved();
        if(products!=null && !products.isEmpty() && !products.equals("undefined"))
         producers =filterByProducts(producers, products);
        if(province!=null && !province.isEmpty() && !province.equals("undefined"))
            producers = filterByProvince(producers, province);
        return createListProducerDetails(producers);
    }

    private List<Producer> filterByProvince(List<Producer> producers, String province) {
        return producers.stream().filter(producer -> producer.getProvince().equals(province)).toList();
    }


    private List<Producer> filterByProducts(List<Producer> producers, String products) {
        return producers.stream()
                .filter(producer -> producer.getProducts()!=null)
                .filter(producer -> producer.getProducts().contains(products))
                .toList();
    }


    public List<ProducerDetails> createListProducerDetails(List<Producer> producers){
        return producers.stream().map(this::createDetails).toList();
    }

    public ProducerDetails createDetails(Producer producer){
        Image image = imageRepository.findByIDOwnerAndType(producer.getID(), TypeImage.COVER);
        ProducerDetails producerDetails = new ProducerDetails();
        if(image!=null)
            producerDetails.setCoverPhoto(image.getDataImage());
        producerDetails.setID(producer.getID());
        producerDetails.setNome(producer.getNome());
        producerDetails.setEmail(producer.getEmail());
        producerDetails.setProvince(producer.getProvince());
        producerDetails.setCity(producer.getCity());
        producerDetails.setAddress(producer.getAddress());
        producerDetails.setPhone(producer.getPhone());
        producerDetails.setProducts(producer.getProducts());
        producerDetails.setDescription(producer.getDescription());
        producerDetails.setLinkFacebook(producer.getLinkFacebook());
        producerDetails.setLinkInstagram(producer.getLinkInstagram());
        producerDetails.setLinkWebsite(producer.getLinkWebsite());
        return producerDetails;
    }

    private void validateUserCreation(UserRequest userRequest) {
        validateProperty("email", userRequest.getEmail(), "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", "Invalid email address");
    }

    private void validateEmail(String email) {
        if (subscribeRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email già presente nei nostri sistemi");
        }
    }

    private void validateProperty(String propertyName, String propertyValue, String regexPattern, String errorMessage) {
        if (propertyValue == null || (regexPattern != null && !Pattern.matches(regexPattern, propertyValue))) {
            throw new IllegalArgumentException(propertyName + ": " + errorMessage);
        }
    }


    private String createID(){
        return UUID.randomUUID().toString();
    }


}
