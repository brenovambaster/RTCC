package com.rtcc.demo.model;

import com.rtcc.demo.DTOs.CourseRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "course")
@Entity(name = "course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String codeOfCourse;

    public Course(CourseRequestDTO courseRequestDTO) {
        this.name = courseRequestDTO.name();
        this.codeOfCourse = courseRequestDTO.codeOfCourse();
    }
}
