package com.example.pnmbackend.request;

import com.example.pnmbackend.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.Serializable;


@Getter
@Setter
public class UserRequest implements Serializable {

    @JsonProperty("email")
    public String email;

    public String toJson(){
      return  JsonUtils.toJson(this);
    }

}
