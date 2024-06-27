package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import com.example.backend.service.AuthService;

@RestController
public class AuthController {
    @Autowired
    public AuthService authService;
    // RedirectView -> redirects the user's url to whatever url inputed

    @GetMapping("/login")
    public RedirectView goToAuthURL(){
        System.out.println("Redirecting to Spotify login");
        String authUrl = authService.authUrl();
        return new RedirectView(authUrl);
    }


    @GetMapping("/callback")
    public RedirectView handleAuthorizationCallback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state) {
        ResponseEntity<String> response = authService.requestAccessToken(code, "http://localhost:8888/callback");
        if (response.getStatusCode().is2xxSuccessful()) {
            //return "Token received: " + response.getBody();
            return new RedirectView("/callback.html");
        } else {
            //return "Failed to obtain token";
            return new RedirectView("/error.html");
        }
    }




}
