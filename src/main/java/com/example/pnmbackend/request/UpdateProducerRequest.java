package com.example.pnmbackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateProducerRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("province")
    private String province;

    @JsonProperty("postalCode")
    private Integer postalCode;

    @JsonProperty("products")
    private String products;

    @JsonProperty("description")
    private String description;

    @JsonProperty("linkFacebook")
    private String linkFacebook;

    @JsonProperty("linkInstagram")
    private String linkInstagram;

    @JsonProperty("linkWebsite")
    private String linkWebsite;



}
