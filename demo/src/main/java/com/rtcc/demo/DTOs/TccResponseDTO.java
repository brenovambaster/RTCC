package com.rtcc.demo.DTOs;

import java.time.LocalDate;
import java.util.List;

public record TccResponseDTO(String id,
                             String title,
                             String author,
                             CourseResponseDTO course,
                             LocalDate defenseDate,
                             String language,
                             ProfessorResponseDTO advisor,
                             List<ProfessorResponseDTO> committeeMembers,
                             String summary,
                             String abstractText,
                             List<KeywordsResponseDTO> keywords,
                             String pathFile,
                             int numLikes,
                             int numFavorites
) {
}
