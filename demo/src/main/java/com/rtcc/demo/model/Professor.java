package com.rtcc.demo.model;

import com.rtcc.demo.DTOs.ProfessorRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "professor")
@Entity(name = "professor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String researchArea;
    private String title;
    private String email;
    private String locationOfWork;

    public Professor(ProfessorRequestDTO data) {
        this.name = data.name();
        this.researchArea = data.researchArea();
        this.title = data.title();
        this.email = data.email();
        this.locationOfWork = data.locationOfWork();
    }


}
