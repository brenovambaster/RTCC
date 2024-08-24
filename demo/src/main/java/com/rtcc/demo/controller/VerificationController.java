package com.rtcc.demo.controller;

import com.rtcc.demo.services.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify-email")
public class VerificationController {
    @Autowired
    private VerificationService verificationService;

    @GetMapping()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public String verifyEmail(@RequestParam String token) {
        return verificationService.verifyEmail(token);
    }
}
