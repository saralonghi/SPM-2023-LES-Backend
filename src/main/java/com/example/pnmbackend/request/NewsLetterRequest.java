package com.example.pnmbackend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsLetterRequest {

    @JsonProperty("producerID")
    public String producerID;

    @JsonProperty("titolo")
    public String titolo;

    @JsonProperty("contenuto")
    public String contenuto;

}
