package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.type.StatusNewsLetter;
import com.example.pnmbackend.repository.NewsLetterRepository;
import com.example.pnmbackend.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class NewsLetterService {

    @Autowired
    private NewsLetterRepository newsLetterRepository;

    @Autowired
    private ProducerRepository producerRepository;



    public void createNewsLetter (NewsLetter newsletter, MultipartFile image) throws IOException {
        Producer producer = getLoggedProducer(SecurityContextHolder.getContext().getAuthentication().getName());
        newsletter.setID(UUID.randomUUID().toString());
        newsletter.setCreated(LocalDateTime.now());
        newsletter.setProducerID(producer.getID());
        newsletter.setStatus(StatusNewsLetter.PENDING.name());
        if(!"null".equalsIgnoreCase(image.getOriginalFilename()))
         newsletter.setImage(image.getBytes());
        validateCreationNewsLetter(newsletter);
        newsLetterRepository.save(newsletter);
    }


    private Producer getLoggedProducer(String name) {
        return producerRepository.findByEmail(name).orElse(null);
    }

    private void validateCreationNewsLetter(NewsLetter newsletter) {
        validateProperty("producerID", newsletter.getProducerID(), null, "Invalid ID format");
        validateProperty("titolo", newsletter.getTitolo(), null, "cannot be empty");
        validateProperty("contenuto", newsletter.getContenuto(), null, "Invalid fiscal format");
    }


    private void validateProperty(String propertyName, String propertyValue, String regexPattern, String errorMessage) {
        if (propertyValue == null || (regexPattern != null && !Pattern.matches(regexPattern, propertyValue))) {
            throw new IllegalArgumentException(propertyName + ": " + errorMessage);
        }
    }


    public List<NewsLetter> retrieveNewsletterProducer(String id) {
        return newsLetterRepository.findByProducerID(id);
    }

    public void deleteProducerNewsletter(String newsletterId) {
        Optional<NewsLetter> optionalNewsLetter = newsLetterRepository.findById(newsletterId);
        if (optionalNewsLetter.isPresent()) {
            newsLetterRepository.deleteById(newsletterId);
        } else {
            throw new NoSuchElementException("NewsLetter with ID " + newsletterId + " not found");
        }
    }






}
