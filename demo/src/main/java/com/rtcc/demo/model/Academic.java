package com.rtcc.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "academic")
@Table(name = "academic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Academic {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Academic(User user, Course course) {
        this.user = user;
        this.course = course;
    }
}
