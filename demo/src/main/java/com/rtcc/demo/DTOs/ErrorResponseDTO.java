package com.rtcc.demo.DTOs;

public class ErrorResponseDTO {
    private String message;
    private int statusCode;

    public ErrorResponseDTO(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
