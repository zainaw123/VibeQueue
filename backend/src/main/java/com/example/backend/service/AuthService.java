package com.example.backend.service;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;


@Service
public class AuthService {
    private String clientId;
    private String clientSecret;
    private final String redirectUrl  = "http://localhost:8888/callback";

    //private WebClient webClient;
    private RestTemplate restTemplate;



    public AuthService(@Value("${spotify.client.id}")String clientId, @Value("${spotify.client.secret}")String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restTemplate = new RestTemplate();
    }

    public String generateState(){
        RandomStringGenerator randomGen = new RandomStringGenerator.Builder().withinRange('a','z').build();
        return randomGen.generate(24);
    }

    //builds the url where the user will need to authorize
    public String authUrl(){
        return UriComponentsBuilder.fromUriString("https://accounts.spotify.com/authorize")
                .queryParam("response_type","code")
                .queryParam("client_id",clientId)
                .queryParam("redirect_uri",redirectUrl)
                .queryParam("state",generateState())
                .queryParam("show_dialog","true")
                .build()
                .toUriString();
    }

    public ResponseEntity<String> requestAccessToken(String code, String redirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String creds = clientId + ":" + clientSecret;
        String encodedCreds = Base64.getEncoder().encodeToString(creds.getBytes(StandardCharsets.UTF_8));
        headers.add("Authorization", "Basic " + encodedCreds);

        String body = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + redirectUri;
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity("https://accounts.spotify.com/api/token", request, String.class);
    }


















}
