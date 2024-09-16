package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.*;
import com.rtcc.demo.exception.EntityNotFoundException;
import com.rtcc.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "API to manage users")
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/change-password/{id}")
    @Operation(summary = "Update user password by ID",
            description = "Update a user password by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "user password updated successfully"),
                    @ApiResponse(responseCode = "404", description = "user not found"),
                    @ApiResponse(responseCode = "500", description = "New password does not match with old password " +
                            "or new password and newPasswordConfirmation does not match")
            }
    )
    public ResponseEntity<String> updateUserPassword(@PathVariable String id,
                                                     @RequestBody PasswordRequestDTO data) {
        try {
            boolean userResponse = userService.updateUserPassword(id, data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok().build();

    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/reset-password")
    @Operation(summary = "Reset user password by email",
            description = "Reset a user password by its email, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "user password reset successfully"),
                    @ApiResponse(responseCode = "404", description = "user not found")
            }
    )
    public ResponseEntity<Void> resetPassword(@RequestParam String email) {
        try {
            userService.resetPassword(email);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }
}
