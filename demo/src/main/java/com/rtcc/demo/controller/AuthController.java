package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.AuthenticationDTO;
import com.rtcc.demo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO authRequestDTO) {

        return ResponseEntity.ok(authService.login(authRequestDTO));
    }
}
