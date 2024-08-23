package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.AcademicRequestDTO;
import com.rtcc.demo.DTOs.AcademicResponseDTO;
import com.rtcc.demo.exception.EntityNotFoundException;
import com.rtcc.demo.model.Academic;
import com.rtcc.demo.model.Course;
import com.rtcc.demo.model.User;
import com.rtcc.demo.repository.AcademicRepository;
import com.rtcc.demo.repository.CoordinatorRepository;
import com.rtcc.demo.repository.CourseRepository;
import com.rtcc.demo.repository.UserRepository;
import com.rtcc.demo.util.TokenGenerator;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicService {

    private AcademicRepository academicRepository;
    private CourseRepository courseRepository;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private EmailService emailService;

    public AcademicService(AcademicRepository a, CourseRepository c, PasswordEncoder p, UserRepository u, EmailService e) {
        this.academicRepository = a;
        this.courseRepository = c;
        this.passwordEncoder = p;
        this.userRepository = u;
        this.emailService = e;

    }

    public void createAcademic(AcademicRequestDTO data) throws MessagingException {
        Logger logger = LoggerFactory.getLogger(CoordinatorService.class);
        // Busca o curso e trata o erro se o curso não for encontrado
        Course course = courseRepository.findById(data.course())
                .orElseThrow(() -> new EntityNotFoundException("Course ", data.course()));

        // Codifica a senha
        String encodedPassword = passwordEncoder.encode(data.password());

        // Cria o usuário
        User user = new User(data.name(), data.email(), encodedPassword);
        user.setVerificationToken(TokenGenerator.generateVerificationToken());

        // Salva o usuário no banco de dados
        User savedUser = userRepository.save(user);

        Academic academic = new Academic(savedUser, course);
        academic.setId(savedUser.getId());
        academicRepository.save(academic);

        // Envio de e-mail de verificação fora da transação
        try {
            emailService.sendVerificationEmail(savedUser);
        } catch (MessagingException e) {
            logger.error("Error sending email: {}", e.getMessage());
            throw new MessagingException("Coordinator created but failed to send verification email: " + e.getMessage());
        }

    }

    public List<AcademicResponseDTO> getAllAcademics() {
        return academicRepository.findAll().stream().map(AcademicResponseDTO::new).toList();
    }

    public AcademicResponseDTO getAcademic(String id) {
        Academic academic = academicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Academic ", id));
        return new AcademicResponseDTO(academic);
    }

    public boolean deleteAcademic(String id) {
        if (academicRepository.existsById(id)) {
            academicRepository.deleteById(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;

    }

    /**
     * Pode atualizar apenas nome e curso. Atualizar senha será um endpoint a parte.
     *
     * @param id   String UUID
     * @param data AcademicRequestDTO
     * @return
     */
    public Optional<AcademicResponseDTO> updateAcademic(String id, AcademicRequestDTO data) {
        return academicRepository.findById(id).map(academic1 -> {
            Course course = courseRepository.findById(data.course())
                    .orElseThrow(() -> new EntityNotFoundException("Course ", data.course()));

            User userUpdate = userRepository.findById(id).map(user -> {
                user.setName(data.name());
                return userRepository.save(user);

            }).orElseThrow(() -> new EntityNotFoundException("User ", id));

            academic1.setUser(userUpdate);
            academic1.setCourse(course);
            Academic updatedAcademic = academicRepository.save(academic1);
            return new AcademicResponseDTO(updatedAcademic);
        });
    }
}
