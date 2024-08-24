package com.rtcc.demo.DTOs;

public record PasswordRequestDTO(String oldPassword, String newPassword, String newPasswordConfirmation) {
}
