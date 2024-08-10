package com.rtcc.demo.DTOs;

import java.time.LocalDate;
import java.util.List;


public record TccRequestDTO(
        String title,
        String author,
        String course,
        LocalDate defenseDate,
        String language,
        String advisor,
        List<String> committeeMembers,
        String summary,
        String abstractText,
        String keywords,
        String pathFile
) {
}