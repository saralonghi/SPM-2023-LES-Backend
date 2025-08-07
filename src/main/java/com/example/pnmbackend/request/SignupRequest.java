package com.example.pnmbackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SignupRequest implements Serializable {

    @JsonProperty("nome")
    public String nome;
    @JsonProperty("email")
    public String email;
    @JsonProperty("password")
    public String password;
    @JsonProperty("fiscalid")
    public String fiscalid;
    @JsonProperty("province")
    private String province;
    @JsonProperty("city")
    private String city;
    @JsonProperty("postalCode")
    private Integer postalCode;
    @JsonProperty("address")
    private String address;
    @JsonProperty("phone")
    private String phone;
}
