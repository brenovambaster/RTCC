package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.PasswordRequestDTO;
import com.rtcc.demo.exception.EntityNotFoundException;
import com.rtcc.demo.model.User;
import com.rtcc.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public boolean updateUserPassword(String id, PasswordRequestDTO data) {

        if (!data.newPassword().equals(data.newPasswordConfirmation())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        User user =  userRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("User not found", ""));

        if (!passwordEncoder.matches(data.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(data.newPassword()));
        User updatedUser = userRepository.save(user);

        return true;


    }
}
