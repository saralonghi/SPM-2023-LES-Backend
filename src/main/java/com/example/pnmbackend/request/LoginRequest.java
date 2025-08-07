package com.example.pnmbackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequest implements Serializable {

    @JsonProperty("email")
    public String email;
    @JsonProperty("password")
    public String password;

}
