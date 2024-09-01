package com.rtcc.demo.DTOs;

public record LikeTccResponseDTO(AcademicResponseDTO academicResponseDTO, TccResponseDTO tccResponseDTO) {

    public LikeTccResponseDTO(AcademicResponseDTO academicResponseDTO, TccResponseDTO tccResponseDTO) {
        this.academicResponseDTO = academicResponseDTO;
        this.tccResponseDTO = tccResponseDTO;
    }
}
