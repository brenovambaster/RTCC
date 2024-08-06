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
    String id;
    String name;
    String email;
    String username;
    String password;
    String course;

    public Coordinator(CoordinatorRequestDTO coordinatorRequestDTO) {
        this.name = coordinatorRequestDTO.name();
        this.email = coordinatorRequestDTO.email();
        this.username = coordinatorRequestDTO.username();
        this.password = coordinatorRequestDTO.password();
        this.course = coordinatorRequestDTO.course();
    }
}
