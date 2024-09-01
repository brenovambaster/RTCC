package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.CoordinatorRequestDTO;
import com.rtcc.demo.DTOs.CoordinatorResponseDTO;

import com.rtcc.demo.exception.EmailNotAvailableException;
import com.rtcc.demo.exception.EntityNotFoundException;
import com.rtcc.demo.infra.config.UserRole;
import com.rtcc.demo.model.Coordinator;
import com.rtcc.demo.model.Course;
import com.rtcc.demo.model.User;
import com.rtcc.demo.repository.CoordinatorRepository;
import com.rtcc.demo.repository.CourseRepository;
import com.rtcc.demo.repository.UserRepository;
import com.rtcc.demo.util.CheckDataCoordinator;
import com.rtcc.demo.util.TokenGenerator;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;


    @Transactional
    public void createCoordinator(CoordinatorRequestDTO data) throws MessagingException {
        Logger logger = LoggerFactory.getLogger(CoordinatorService.class);
        logger.info("------Creating coordinator: {}", data);

        // Validação dos dados do coordenador
        CheckDataCoordinator.checkDataCreate(data);

        // Busca o curso e trata o erro se o curso não for encontrado
        Course course = courseRepository.findById(data.course())
                .orElseThrow(() -> new EntityNotFoundException("Course ", data.course()));

        if (userRepository.existsByEmail(data.email())) {
            throw new EmailNotAvailableException("Email already in use");
        }

        // Codifica a senha
        String encodedPassword = passwordEncoder.encode(data.password());

        // Cria o usuário
        User user = new User(data.name(), data.email(), encodedPassword, UserRole.COORDINATOR);
        user.setVerificationToken(TokenGenerator.generateVerificationToken());

        // Salva o usuário no banco de dados
        User savedUser = userRepository.save(user);

        logger.info("User created with ID: {}", savedUser.getId());

        // Cria o coordenador associando ao curso e usuário criado
        Coordinator coordinator = new Coordinator(savedUser, course);
        coordinator.setId(savedUser.getId());
        coordinatorRepository.save(coordinator);

        // Envio de e-mail de verificação fora da transação
        try {
            emailService.sendVerificationEmail(savedUser);
        } catch (MessagingException e) {
            logger.error("Error sending email: {}", e.getMessage());
            throw new MessagingException("Coordinator created but failed to send verification email: " + e.getMessage());
        }
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
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<CoordinatorResponseDTO> updateCoordinator(String id, CoordinatorRequestDTO data) {

        return coordinatorRepository.findById(id).map(coordinator -> {
//            CheckDataCoordinator.checkDataUpdate(data);
            Course course = courseRepository.findById(data.course())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + data.course()));

            String encodedPassword = passwordEncoder.encode(data.password());

            User userUpdate = userRepository.findById(id)
                    .map(user -> {
                        user.setName(data.name());
                        user.setEmail(data.email());
                        if (!data.password().isEmpty()) {
                            user.setPassword(encodedPassword);
                        }
                        return userRepository.save(user);
                    })
                    .orElseThrow(() -> new EntityNotFoundException("User: ", id));

            coordinator.setUser(userUpdate);
            coordinator.setCourse(course);

            Coordinator updatedCoordinator = coordinatorRepository.save(coordinator);

            return new CoordinatorResponseDTO(updatedCoordinator);

        });
    }

}
