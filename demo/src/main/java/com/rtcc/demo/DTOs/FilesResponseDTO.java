package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Files;

public record FilesResponseDTO(String id,String name, String type, String path) {

    public FilesResponseDTO(Files file) {
        this(file.getId(),file.getName(), file.getType(), file.getPath());
    }
}
