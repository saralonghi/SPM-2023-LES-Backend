package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.Admin;
import com.example.pnmbackend.model.entities.Image;
import com.example.pnmbackend.model.entities.News;
import com.example.pnmbackend.model.type.TypeImage;
import com.example.pnmbackend.repository.AdminRepository;
import com.example.pnmbackend.repository.ImageRepository;
import com.example.pnmbackend.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NewsService {


    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AdminRepository adminRepository;


    public void createNews (News news, MultipartFile[] images) throws IOException {

        Admin admin = getLoggedAdmin(SecurityContextHolder.getContext().getAuthentication().getName());
        news.setCreated(LocalDateTime.now());
        news.setID(UUID.randomUUID().toString());
        news.setAdminID(admin.getID());
        saveImages(images, news.getID());

        newsRepository.save(news);


    }

    public void deleteNews(String id) {
        newsRepository.deleteById(id);
    }


    private Admin getLoggedAdmin(String name) {
        return adminRepository.findByEmail(name).orElse(null);
    }

    private void saveImages(MultipartFile[] images, String newsID) throws IOException {
        for (MultipartFile image : images) {
            Image img = new Image();
            img.setID(UUID.randomUUID().toString());
            img.setIDOwner(newsID);
            img.setName(image.getOriginalFilename());
            img.setDataImage(image.getBytes());
            img.setType(TypeImage.NEWS);
            imageRepository.save(img);
        }
    }

    public List<News> retrieveAllNews() {
        return newsRepository.findAll().stream().sorted().collect(Collectors.toList());
    }

    public News retrieveNews(String id) {
        return newsRepository.findById(id).orElse(null);
    }
}
