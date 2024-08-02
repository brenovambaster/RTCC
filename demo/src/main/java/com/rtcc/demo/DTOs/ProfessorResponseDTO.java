package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Professor;

public record ProfessorResponseDTO(String id, String name, String researchArea, String title, String email,
                                   String locationOfWork) {

    public ProfessorResponseDTO(Professor professor) {
        this(professor.getId(), professor.getName(), professor.getResearchArea(), professor.getTitle(), professor.getEmail(), professor.getLocationOfWork());
    }
}
