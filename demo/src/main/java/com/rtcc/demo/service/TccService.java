package com.rtcc.demo.service;

import com.rtcc.demo.DTOs.TccRequestDTO;
import com.rtcc.demo.model.Course;
import com.rtcc.demo.model.Professor;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.repository.CourseRepository;
import com.rtcc.demo.repository.ProfessorRepository;
import com.rtcc.demo.repository.TccRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TccService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private TccRepository tccRepository;

    public TccService(CourseRepository courseRepository, ProfessorRepository professorRepository) {
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
    }

    public Tcc convertToEntity(TccRequestDTO dto) {
        Course course = courseRepository.findById(dto.course())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Professor advisor = professorRepository.findById(dto.advisor())
                .orElseThrow(() -> new RuntimeException("Advisor not found"));

        List<String> committeeMemberIds = dto.committeeMembers().stream()
                .collect(Collectors.toList());

        Set<Professor> committeeMembers = new HashSet<>(professorRepository.findAllById(committeeMemberIds));
        return new Tcc(
                null,
                dto.title(),
                dto.pathFile(),
                dto.author(),
                course,
                dto.defenseDate(),
                dto.language(),
                advisor,
                committeeMembers,
                dto.summary(),
                dto.abstractText(),
                dto.keywords()
        );
    }

    public Tcc save(Tcc tcc) {
        return tccRepository.save(tcc);
    }

}
