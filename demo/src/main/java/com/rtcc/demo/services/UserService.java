package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.PasswordRequestDTO;
import com.rtcc.demo.exception.EntityNotFoundException;
import com.rtcc.demo.infra.config.PasswordGenerator;
import com.rtcc.demo.model.User;
import com.rtcc.demo.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    private final PasswordGenerator passwordGenerator;

    @Autowired
    private EmailService emailService;

    public UserService(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    public boolean updateUserPassword(String id, PasswordRequestDTO data) {

        if (!data.newPassword().equals(data.newPasswordConfirmation())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        User user = userRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));

        if (!passwordEncoder.matches(data.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(data.newPassword()));
        userRepository.save(user);
        return true;

    }

    /**
     * Reset user password by email and send it by email
     *
     * @param email
     * @throws MessagingException
     */
    public void resetPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));

        String newPassword = passwordGenerator.generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        String emailBody = "Sua nova senha é: " + newPassword + ".<br/><br/>" +
                "Por favor, altere sua senha assim que possível.";
        emailService.sendEmail(user.getEmail(), "ALTERAÇÃO DE SENHA", emailBody, "Nova senha");
    }


}
