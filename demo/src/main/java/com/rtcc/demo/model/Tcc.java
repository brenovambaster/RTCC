package com.rtcc.demo.model;

import com.rtcc.demo.DTOs.TccRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tcc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tcc {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String pathFile;

    @Column(nullable = false)
    private String author;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "defense_date", nullable = false)
    private LocalDate defenseDate;

    @Column(length = 50, nullable = false)
    private String language;

    @ManyToOne
    @JoinColumn(name = "advisor", nullable = false)
    private Professor advisor;

    @ManyToMany
    @JoinTable(
            name = "tcc_committee_members",
            joinColumns = @JoinColumn(name = "tcc_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private Set<Professor> committeeMembers = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String abstractText;

    @Column(columnDefinition = "TEXT")
    private String keywords;

    public Tcc(TccRequestDTO data) {

    }
}