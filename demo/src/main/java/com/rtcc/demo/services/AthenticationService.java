package com.rtcc.demo.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AthenticationService {
    private final JwtService jwtService;

    public AthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    public String authenticate(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

}
