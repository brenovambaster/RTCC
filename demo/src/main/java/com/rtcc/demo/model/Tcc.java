package com.rtcc.demo.model;

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
    @JoinColumn(name = "course", nullable = false)
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

    @Column(columnDefinition = "TEXT", name = "abstract")
    private String abstractText;

    @ManyToMany
    @JoinTable(
            name = "tcc_keywords",
            joinColumns = @JoinColumn(name = "tcc_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_name")
    )
    private Set<Keywords> keywords = new HashSet<>();

    @Column(name = "num_likes")
    private int numLikes;
    @Column(name = "num_favorites")
    private int numFavorites;

    public Tcc(Object o, String title, String s, String author, Course course, LocalDate localDate, String language,
               Professor advisor, Set<Professor> committeeMembers, String summary, String abstractText, Set<Keywords> keywordsSet) {

        this.title = title;
        this.pathFile = s;
        this.author = author;
        this.course = course;
        this.defenseDate = localDate;
        this.language = language;
        this.advisor = advisor;
        this.committeeMembers = committeeMembers;
        this.summary = summary;
        this.abstractText = abstractText;
        this.keywords = keywordsSet;

    }

    public void addLike() {
        this.numLikes++;
    }

    public void removeLike() {
        if (this.numLikes > 0) {
            this.numLikes--;
        }
    }

    public void addFavorite() {
        this.numFavorites++;
    }

    public void removeFavorite() {
        if (this.numFavorites > 0) {
            this.numFavorites--;
        }
    }
}
