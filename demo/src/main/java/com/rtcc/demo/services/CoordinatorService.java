package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.CoordinatorRequestDTO;
import com.rtcc.demo.DTOs.CoordinatorResponseDTO;
import com.rtcc.demo.model.Coordinator;
import com.rtcc.demo.model.Course;
import com.rtcc.demo.repository.CoordinatorRepository;
import com.rtcc.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordinatorService {

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Coordinator createCoordinator(CoordinatorRequestDTO data) {
        Course course = courseRepository.findById(data.course())
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + data.course()));

        String encodedPassword = passwordEncoder.encode(data.password());

        Coordinator coordinator = new Coordinator(
                data.name(),
                data.email(),
                data.username(),
                encodedPassword,
                course
        );

        return coordinatorRepository.save(coordinator);
    }

    public List<CoordinatorResponseDTO> getAllCoordinators() {

        return coordinatorRepository.findAll()
                .stream()
                .map(CoordinatorResponseDTO::new)
                .toList();
    }

    public Optional<CoordinatorResponseDTO> getCoordinatorById(String id) {
        Coordinator coordinator = coordinatorRepository.findById(id).orElse(null);

        if (coordinator == null) {
            return Optional.empty();
        }
        return Optional.of(new CoordinatorResponseDTO(coordinator));
    }

    public boolean deleteCoordinatorById(String id) {
        if (coordinatorRepository.existsById(id)) {
            coordinatorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<CoordinatorResponseDTO> updateCoordinator(String id, CoordinatorRequestDTO data) {
        return coordinatorRepository.findById(id).map(coordinator -> {
            Course course = courseRepository.findById(data.course())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + data.course()));

            coordinator.setName(data.name());
            coordinator.setEmail(data.email());
            coordinator.setUsername(data.username());
            coordinator.setPassword(passwordEncoder.encode(data.password()));
            coordinator.setCourse(course);

            Coordinator updatedCoordinator = coordinatorRepository.save(coordinator);
            return new CoordinatorResponseDTO(updatedCoordinator);
        });
    }
}
