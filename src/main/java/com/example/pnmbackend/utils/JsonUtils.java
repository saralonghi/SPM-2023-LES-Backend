package com.example.pnmbackend.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.NoArgsConstructor;


import java.io.IOException;


@NoArgsConstructor
public class JsonUtils {


    private static ObjectMapper mapperToJsonInit() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        simpleFilterProvider.setFailOnUnknownId(false);
        objectMapper.setFilterProvider(simpleFilterProvider);

        return objectMapper;
    }

    private static ObjectMapper mapperToObjectInit() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        simpleFilterProvider.setFailOnUnknownId(false);
        objectMapper.setFilterProvider(simpleFilterProvider);

        return objectMapper;
    }

    public static String toJson(Object object) {
        ObjectMapper mapperToJson = mapperToJsonInit();

        String str = null;
        try {
            str = mapperToJson.writeValueAsString(object);
        } catch (IOException ioException) {
            return str;
        }
        return str;
    }


    public static <T> T toObject(String jsonInString, Class<T> aClass) {
        ObjectMapper mapperToObject = mapperToObjectInit();

        T object = null;
        try {
            object = mapperToObject.readValue(jsonInString.getBytes(), aClass);
        } catch (IOException ioException) {
            return object;
        }
        return object;
    }
}
