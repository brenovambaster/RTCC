package com.rtcc.demo.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.rtcc.demo.DTOs.CourseRequestDTO;
import com.rtcc.demo.DTOs.CourseResponseDTO;
import com.rtcc.demo.exception.CourseDeletionException;
import com.rtcc.demo.exception.EntityDeletionException;
import com.rtcc.demo.model.Course;
import com.rtcc.demo.repository.CourseRepository;
import com.rtcc.demo.repository.TccRepository;
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
@RequestMapping("/course")

@Tag(name = "Courses", description = "API to manage courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TccRepository tccRepository;


    @Operation(summary = "Save a new course",
            description = "Save a new course with the provided details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Course saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> saveCourse(@RequestBody CourseRequestDTO data) {
        Course course = new Course(data);
        courseRepository.save(course);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all courses",
            description = "Retrieve a list of all courses.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of courses", content = @Content(schema = @Schema(implementation = CourseResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courseList = courseRepository.findAll()
                .stream()
                .map(CourseResponseDTO::new)
                .toList();
        return ResponseEntity.ok(courseList);
    }

    @Operation(summary = "Get a course by ID",
            description = "Retrieve a course by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Course found", content = @Content(schema = @Schema(implementation = CourseResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable String id) {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CourseResponseDTO courseResponse = new CourseResponseDTO(courseOpt.get());
        return ResponseEntity.ok(courseResponse);
    }

    @Operation(summary = "Delete a course by ID",
            description = "Delete a course by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        boolean isLinkedToTcc = tccRepository.existsByCourseId(id);
        if (isLinkedToTcc) {
            throw new EntityDeletionException("Curso", "Não pode deletar um curso vinculado a um ou vários TCC");
        }

        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    @Operation(summary = "Update course by ID",
            description = "Update a course by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Course updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Course not found")
            }
    )
    public ResponseEntity<CourseResponseDTO> updateCurso(@PathVariable String id, @RequestBody CourseRequestDTO data) {
        Optional<Course> cursoOptional = courseRepository.findById(id);

        if (cursoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course curso = cursoOptional.get();

        curso.setName(data.name());
        curso.setCodeOfCourse(data.codeOfCourse());

        courseRepository.save(curso);

        CourseResponseDTO cursoResponse = new CourseResponseDTO(curso);
        return ResponseEntity.ok().body(cursoResponse);
    }
}
