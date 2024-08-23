package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Academic;
import com.rtcc.demo.model.Course;

public record AcademicResponseDTO(String id, String name, String email, String password,
                                  Course course) {
    public AcademicResponseDTO(Academic academic) {
        this(academic.getId(), academic.getUser().getName(), academic.getUser().getEmail(), academic.getUser().getPassword(), academic.getCourse());
    }
}
