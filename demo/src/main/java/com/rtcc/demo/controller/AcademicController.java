package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.AcademicRequestDTO;
import com.rtcc.demo.DTOs.AcademicResponseDTO;
import com.rtcc.demo.DTOs.CoordinatorResponseDTO;
import com.rtcc.demo.DTOs.PasswordRequestDTO;
import com.rtcc.demo.repository.AcademicRepository;

import com.rtcc.demo.services.AcademicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/academic")
public class AcademicController {
    @Autowired
    private AcademicRepository academicRepository;
    @Autowired
    private AcademicService academicService;


    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> saveAcademic(@RequestBody AcademicRequestDTO academic) throws MessagingException {
        academicService.createAcademic(academic);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<AcademicResponseDTO>> getAllAcademics() {
        return ResponseEntity.ok(academicService.getAllAcademics());
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<AcademicResponseDTO> getAcademic(@PathVariable String id) {
        return ResponseEntity.ok(academicService.getAcademic(id));
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> deleteAcademic(@PathVariable String id) {
        academicService.deleteAcademic(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<AcademicResponseDTO> updateAcademic(@PathVariable String id, @RequestBody AcademicRequestDTO academic) {
        Optional<AcademicResponseDTO> academicOptional = academicService.updateAcademic(id, academic);
        return academicOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/change-password/{id}")
    @Operation(summary = "Update Academic password by ID",
            description = "Update a Academic password by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Academic updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Academic not found")
            }
    )
    public ResponseEntity<AcademicResponseDTO> updateAcademicPassword(@PathVariable String id,
                                                                            @RequestBody PasswordRequestDTO data) {
        Optional<AcademicResponseDTO> academicResponse = academicService.updateAcademicPassword(id, data);
        return academicResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
