package com.rtcc.demo.model;

import com.rtcc.demo.DTOs.CoordinatorRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "coordinator")
@Entity(name = "coordinator")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coordinator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
    private String username;
    private String password;

    @OneToOne
    @JoinColumn(name = "course")
    private Course course;

    public Coordinator(String name, String email, String username, String password, Course course) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.course = course;
    }
}
