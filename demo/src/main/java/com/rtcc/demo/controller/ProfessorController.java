package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.ProfessorRequestDTO;
import com.rtcc.demo.DTOs.ProfessorResponseDTO;
import com.rtcc.demo.model.Professor;
import com.rtcc.demo.repository.ProfessorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professor")
@Tag(name = "Professor", description = "API to manage professors")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    @Operation(summary = "Sava a new professor", description = "Add a new professor into database", responses = {
            @ApiResponse(responseCode = "200", description = "Professor saved successfully")
    })
    public ResponseEntity<Void> saveProfessor(@RequestBody ProfessorRequestDTO data) {
        Professor professorData = new Professor(data);
        professorRepository.save(professorData);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    @Operation(summary = "Returns a list of all professors",
            description = "",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of all professors")
            })
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
    @Operation(summary = "Get professor by ID",
            description = "Get a professor by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "404", description = "Professor not found")
            })
    public ResponseEntity<ProfessorResponseDTO> getProfessor(@PathVariable String id) {
        Optional<Professor> professor = professorRepository.findById(id);

        if (professor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ProfessorResponseDTO professorResponse = new ProfessorResponseDTO(professor.get());
        return ResponseEntity.ok().body(professorResponse);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    @Operation(summary = "Update professor by ID",
            description = "Update a professor by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Professor updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Professor not found")
            })
    public ResponseEntity<ProfessorResponseDTO> updateProfessor(@PathVariable String id, @RequestBody ProfessorRequestDTO data) {
        Optional<Professor> professorOptional = professorRepository.findById(id);

        if (professorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Professor professor = professorOptional.get();

        professor.setName(data.name());
        professor.setEmail(data.email());
        professor.setTitle(data.title());
        professor.setLocationOfWork(data.locationOfWork());
        professor.setResearchArea(data.researchArea());


        professorRepository.save(professor);

        ProfessorResponseDTO professorResponse = new ProfessorResponseDTO(professor);
        return ResponseEntity.ok().body(professorResponse);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove professor by id", description = "Remove a professor by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Professor removed successfully"),
                    @ApiResponse(responseCode = "404", description = "Professor not found")
            })
    public ResponseEntity<Void> deleteProfessor(@PathVariable String id) {
        if (professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Retornar 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Retornar 404 Not Found
        }
    }
}
