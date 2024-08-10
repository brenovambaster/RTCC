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
import java.util.Optional;
import java.util.Set;

@Service
public class TccService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private TccRepository tccRepository;

    public Tcc convertToEntity(TccRequestDTO dto) {
        Course course = courseRepository.findById(dto.course())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Professor advisor = professorRepository.findById(dto.advisor())
                .orElseThrow(() -> new RuntimeException("Advisor not found"));

        List<String> committeeMemberIds = dto.committeeMembers();
        Set<Professor> committeeMembers = new HashSet<>();

        for (String id : committeeMemberIds) {
            professorRepository.findById(id).ifPresent(committeeMembers::add);
        }

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

    public Tcc saveTcc(Tcc tcc) {
        return tccRepository.save(tcc);
    }

    public Optional<Tcc> findById(String id) {
        return tccRepository.findById(id);
    }

    public List<Tcc> findAll() {
        return tccRepository.findAll();
    }

    public void deleteTcc(String id) {
        tccRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return tccRepository.existsById(id);
    }
}
