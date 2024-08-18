package com.rtcc.demo.DTOs;

import com.rtcc.demo.model.Course;

public record CoordinatorRequestDTO(String id, String name, String email, String username, String password,
                                    String course) {

}
