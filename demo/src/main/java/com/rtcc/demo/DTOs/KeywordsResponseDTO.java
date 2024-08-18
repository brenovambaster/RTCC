package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Keywords;

import java.security.Key;

public record KeywordsResponseDTO(String name) {
    public KeywordsResponseDTO(Keywords keywords) {
        this(keywords.getName());
    }


}
