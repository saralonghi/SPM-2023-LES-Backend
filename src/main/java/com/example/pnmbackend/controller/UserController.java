package com.example.pnmbackend.controller;

import com.example.pnmbackend.request.UserRequest;
import com.example.pnmbackend.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/user")
public class UserController {


    @Autowired
    private UserService userService;



    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", "User created successfully");
            userService.createUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toString());
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }


}
