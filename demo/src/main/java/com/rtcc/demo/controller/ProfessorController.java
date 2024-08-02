package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.ProfessorRequestDTO;
import com.rtcc.demo.DTOs.ProfessorResponseDTO;
import com.rtcc.demo.model.Professor;
import com.rtcc.demo.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")

public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public void saveProfessor(@RequestBody ProfessorRequestDTO data) {
        Professor professorData = new Professor(data);
        professorRepository.save(professorData);
        ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<ProfessorResponseDTO> getAll() {
        List<ProfessorResponseDTO> professorList = professorRepository
                .findAll()
                .stream()
                .map(ProfessorResponseDTO::new)
                .toList();
        return professorList;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ProfessorResponseDTO getProfessor(@PathVariable String id) {
        Professor professor = professorRepository.findById(id).orElseThrow();
        return new ProfessorResponseDTO(professor);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public void deleteProfessor(@PathVariable String id) {
        professorRepository.deleteById(id);
        ResponseEntity.ok().build();
    }
}
