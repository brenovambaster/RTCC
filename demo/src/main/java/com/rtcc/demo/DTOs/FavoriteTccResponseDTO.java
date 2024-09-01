package com.rtcc.demo.DTOs;

public record FavoriteTccResponseDTO(String academicId, String tccId) {
    public FavoriteTccResponseDTO(String academicId, String tccId) {
        this.academicId = academicId;
        this.tccId = tccId;
    }
}
