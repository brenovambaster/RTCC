package com.rtcc.demo.services;

import com.rtcc.demo.model.User;
import com.rtcc.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VerificationService {
    @Autowired
    private UserRepository userRepository;

    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid verification token"));

        if (user.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already verified");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);  // Limpar o token após a verificação
        userRepository.save(user);

        return "E-mail verificado com sucesso!";
    }
}
