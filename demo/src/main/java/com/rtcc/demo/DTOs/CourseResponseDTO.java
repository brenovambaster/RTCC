package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Course;

public record CourseResponseDTO(String id, String name, String campus, String codeOfCourse) {

    public CourseResponseDTO(Course course) {
        this(course.getId(), course.getName(), course.getCampus(), course.getCodeOfCourse());
    }
}
