package com.rtcc.demo.controller;

import com.rtcc.demo.services.VerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify-email")
@Tag(name = "Verification", description = "API to verify email")
public class VerificationController {
    @Autowired
    private VerificationService verificationService;

    @GetMapping()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        String result = verificationService.verifyEmail(token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
