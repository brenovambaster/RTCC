package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.CourseRequestDTO;
import com.rtcc.demo.DTOs.CourseResponseDTO;
import com.rtcc.demo.model.Course;
import com.rtcc.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public void saveCourse(@RequestBody CourseRequestDTO data) {
        Course courseData = new Course(data);
        courseRepository.save(courseData);
        ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<CourseResponseDTO> getAll() {
        List<CourseResponseDTO> courseList = courseRepository
                .findAll()
                .stream()
                .map(CourseResponseDTO::new)
                .toList();
        return courseList;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable String id) {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        CourseResponseDTO courseResponse = new CourseResponseDTO(courseOpt.get());
        return ResponseEntity.ok(courseResponse);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable String id) {
        courseRepository.deleteById(id);
        ResponseEntity.ok().build();
    }
}
