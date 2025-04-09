package com.example.paymentsystem.utils;

import com.example.paymentsystem.utils.RestServiceHandler;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClientUtility {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T, R> R postRequest(String url, T requestBody, Class<R> responseType, String serviceName) {

       /* ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        WebTarget webTarget = clientBuilder.build().target(url).register(new RestServiceHandler(serviceName));
   */     // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Wrap request body in HttpEntity
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make POST request
        ResponseEntity<R> response = restTemplate.postForEntity(url, requestEntity, responseType);

        // Return response body
        return response.getBody();
    }
}