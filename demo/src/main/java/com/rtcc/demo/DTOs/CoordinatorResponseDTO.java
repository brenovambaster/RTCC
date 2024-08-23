package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Coordinator;
import com.rtcc.demo.model.Course;

public record CoordinatorResponseDTO(String id, String name, String email, String password,
                                     Course course) {

    public CoordinatorResponseDTO(Coordinator coordinator) {
        this(coordinator.getId(), coordinator.getUser().getName(), coordinator.getUser().getEmail(), coordinator.getUser().getPassword(), coordinator.getCourse());
    }
}