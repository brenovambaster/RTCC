package com.rtcc.demo.services;

import com.rtcc.demo.model.User;
import com.rtcc.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {
    @Autowired
    private UserRepository userRepository;

    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);  // Limpar o token após a verificação
        userRepository.save(user);

        return "E-mail verificado com sucesso!";
    }
}
