package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Coordinator;
import com.rtcc.demo.model.Course;

public record CoordinatorResponseDTO(String id, String name, String email, String username, String password,
                                     Course course) {

    public CoordinatorResponseDTO(Coordinator coordinator) {
        this(coordinator.getId(), coordinator.getName(), coordinator.getEmail(), coordinator.getUsername(), coordinator.getPassword(), coordinator.getCourse());
    }
}