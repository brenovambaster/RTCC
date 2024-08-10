package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Course;
import com.rtcc.demo.model.Professor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record TccRequestDTO(
        String title,
        String author,
        Course course,
        LocalDate defenseDate,
        String language,
        Professor advisor,
        List<String> committeeMembers,
        String summary,
        String abstractText,
        String keywords,
        String pathFile
) {
}