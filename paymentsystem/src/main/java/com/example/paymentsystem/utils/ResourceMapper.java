package com.example.paymentsystem.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.CharStreams;
import org.apache.commons.io.Charsets;
import org.springframework.boot.json.JsonParseException;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceMapper {

        public static <T> T get(String fileName, TypeReference<T> typeReference) {
        try {
            InputStream is = ResourceMapper.class.getClassLoader().getResourceAsStream(fileName);
            String json = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
            return JSONMapper.convert(json, typeReference);
        } catch (Exception e) {//NOSONAR
            throw new JsonParseException(e);
        }
    }
}
