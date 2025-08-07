package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.Image;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.type.TypeImage;
import com.example.pnmbackend.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;
    //logo-------------------------------------------
    public Image retrieveLogo(String id){
        return imageRepository.findByIDOwnerAndType(id,TypeImage.LOGO);
    }
    //Other - images ------------------------------------------------------
    public void saveImageProducer(MultipartFile img, Producer producer) throws IOException {
        Image image = new Image();
        image.setID(UUID.randomUUID().toString());
        image.setIDOwner(producer.getID());
        image.setName(img.getOriginalFilename());

        if (img.getName().equalsIgnoreCase("logo")) {
            Image oldImage = imageRepository.findByIDOwnerAndType(producer.getID(), TypeImage.LOGO);
            if (oldImage != null)
                imageRepository.delete(oldImage);
            image.setType(TypeImage.LOGO);
        } else if (img.getName().equalsIgnoreCase("other")){
            image.setType(TypeImage.OTHER);
        }
        image.setDataImage(img.getBytes());
        imageRepository.save(image);
    }

    public List<Image> retrieveAllImages(String id) {
        return imageRepository.findAllByIDOwner(id).stream()
                .filter(image ->!image.getType().equals(TypeImage.LOGO))
                .toList();
    }

    public void deleteAllImages(String id) {
        imageRepository.deleteAll(imageRepository.findAllByIDOwner(id).stream()
                .filter(image -> image.getType().equals(TypeImage.OTHER))
                .toList());

    }

    public void deleteImage(String id) {
        imageRepository.deleteById(id);
    }

    public Image retrieveCover(String id) {
        return imageRepository.findByIDOwnerAndType(id,TypeImage.COVER);
    }

    //Cover-------------------------------------------------------------------
    public void saveCover(MultipartFile multipartFile, Producer producer) throws IOException {
        Image image = new Image();
        image.setID(UUID.randomUUID().toString());
        image.setIDOwner(producer.getID());
        image.setName(multipartFile.getOriginalFilename());
        Image oldImage = imageRepository.findByIDOwnerAndType(producer.getID(), TypeImage.COVER);
        if (oldImage != null)
            imageRepository.delete(oldImage);
        image.setType(TypeImage.COVER);
        image.setDataImage(multipartFile.getBytes());
        imageRepository.save(image);
    }



}
