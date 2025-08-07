package com.example.pnmbackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChangePasswordRequest implements Serializable {

    @JsonProperty("token")
    public String token;
    @JsonProperty("password")
    public String password;

}
