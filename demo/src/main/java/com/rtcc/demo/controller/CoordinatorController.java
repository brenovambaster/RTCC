package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.CoordinatorRequestDTO;
import com.rtcc.demo.DTOs.CoordinatorResponseDTO;
import com.rtcc.demo.repository.CoordinatorRepository;
import com.rtcc.demo.services.CoordinatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coordinator")

@Tag(name = "Coordinators", description = "API to manage coordinators")
public class CoordinatorController {

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    CoordinatorService coordinatorService;


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
        coordinatorService.createCoordinator(data);
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

        return ResponseEntity.ok(coordinatorService.getAllCoordinators());
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
    public ResponseEntity<Optional<CoordinatorResponseDTO>> getCoordinator(@PathVariable String id) {

        Optional<CoordinatorResponseDTO> coordinatorOpt = coordinatorService.getCoordinatorById(id);

        if (coordinatorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(coordinatorOpt);
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
        boolean response = coordinatorService.deleteCoordinatorById(id);
        if (!response) {
            return ResponseEntity.notFound().build();
        }

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
    public ResponseEntity<CoordinatorResponseDTO> updateCoordinator(@PathVariable String id,
                                                                    @RequestBody CoordinatorRequestDTO data) {
        Optional<CoordinatorResponseDTO> coordinatorResponse = coordinatorService.updateCoordinator(id, data);
        return coordinatorResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
