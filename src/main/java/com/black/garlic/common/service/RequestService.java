package com.black.garlic.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class RequestService {
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    @Transactional
    public Object fetchRequest(HttpHeaders headers, MultiValueMap<String, String> multiValueMap, String endPoint, HttpMethod httpMethod) {
        HttpEntity<Object> entity = new HttpEntity<>(multiValueMap, headers);

        ResponseEntity<Object> response = restTemplate.exchange(endPoint, httpMethod, entity, Object.class);

        return response.getBody();
    }

    @Transactional
    public Object fetchRequestByString(HttpHeaders headers, String string, String endPoint, HttpMethod httpMethod) {
        HttpEntity<String> entity = new HttpEntity<>(string, headers);

        return restTemplate.exchange(endPoint, httpMethod, entity, Object.class).getBody();
    }
}
