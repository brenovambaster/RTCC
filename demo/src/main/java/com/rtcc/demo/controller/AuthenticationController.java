package com.rtcc.demo.controller;

import com.rtcc.demo.services.AthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private final AthenticationService authenticationService;

    public AuthenticationController(AthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public String authenticate(Authentication authentication) {
        System.out.println(authentication);
        return authenticationService.authenticate(authentication);
    }

}
