package com.example.pnmbackend.repository;

import com.example.pnmbackend.model.entities.Image;
import com.example.pnmbackend.model.type.TypeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,String>{

    Image findByIDOwnerAndType(String idOwner, TypeImage typeImage);

    List<Image> findAllByIDOwner(String id);

    Image findByName(String name);
}
