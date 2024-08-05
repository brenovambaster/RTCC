package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.CoordinatorRequestDTO;
import com.rtcc.demo.DTOs.CoordinatorResponseDTO;
import com.rtcc.demo.model.Coordinator;
import com.rtcc.demo.repository.CoordinatorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coordinator")

@Tag(name = "Coordinators", description = "API to manage coordinators")
public class CoordinatorController {

    @Autowired
    private CoordinatorRepository coordinatorRepository;


    @Operation(summary = "Save a new coordinator",
            description = "Save a new coordinator with the provided details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coordinator saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> saveCoordinator(@RequestBody CoordinatorRequestDTO data) {
        Coordinator coordinator = new Coordinator(data);
        coordinatorRepository.save(coordinator);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all coordinators",
            description = "Retrieve a list of all coordinators.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of coordinators", content = @Content(schema = @Schema(implementation = CoordinatorResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<CoordinatorResponseDTO>> getAllCoordinators() {
        List<CoordinatorResponseDTO> coordinatorList = coordinatorRepository.findAll()
                .stream()
                .map(CoordinatorResponseDTO::new)
                .toList();
        return ResponseEntity.ok(coordinatorList);
    }

    @Operation(summary = "Get a coordinator by ID",
            description = "Retrieve a coordinator by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coordinator found", content = @Content(schema = @Schema(implementation = CoordinatorResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Coordinator not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<CoordinatorResponseDTO> getCoordinator(@PathVariable String id) {
        Optional<Coordinator> coordinatorOpt = coordinatorRepository.findById(id);

        if (coordinatorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CoordinatorResponseDTO coordinatorResponse = new CoordinatorResponseDTO(coordinatorOpt.get());
        return ResponseEntity.ok(coordinatorResponse);
    }

    @Operation(summary = "Delete a coordinator by ID",
            description = "Delete a coordinator by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coordinator deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Coordinator not found", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> deleteCoordinator(@PathVariable String id) {
        if (!coordinatorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        coordinatorRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    @Operation(summary = "Update coordinator by ID",
            description = "Update a coordinator by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coordinator updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Coordinator not found")
            }
    )
    public ResponseEntity<CoordinatorResponseDTO> updateCoordinator(@PathVariable String id, @RequestBody CoordinatorRequestDTO data) {
        Optional<Coordinator> coordinatorOptional = coordinatorRepository.findById(id);

        if (coordinatorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Coordinator coordinator = coordinatorOptional.get();

        coordinator.setName(data.name());
        coordinator.setEmail(data.email());
        coordinator.setUsername(data.username());
        coordinator.setPassword(data.password());
        coordinator.setCourse(data.course());

        coordinatorRepository.save(coordinator);

        CoordinatorResponseDTO coordinatorResponse = new CoordinatorResponseDTO(coordinator);
        return ResponseEntity.ok().body(coordinatorResponse);
    }
}
