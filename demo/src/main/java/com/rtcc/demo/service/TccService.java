package com.rtcc.demo.service;

import com.rtcc.demo.DTOs.CourseResponseDTO;
import com.rtcc.demo.DTOs.ProfessorResponseDTO;
import com.rtcc.demo.DTOs.TccRequestDTO;
import com.rtcc.demo.DTOs.TccResponseDTO;
import com.rtcc.demo.model.Course;
import com.rtcc.demo.model.Professor;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.repository.CourseRepository;
import com.rtcc.demo.repository.ProfessorRepository;
import com.rtcc.demo.repository.TccRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class TccService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private TccRepository tccRepository;

    /**
     * Converte um DTO em uma entidade
     *
     * @param dto
     * @return
     */
    public Tcc convertToEntity(TccRequestDTO dto) {
        Course course = courseRepository.findById(dto.course())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Professor advisor = professorRepository.findById(dto.advisor())
                .orElseThrow(() -> new RuntimeException("Advisor not found"));

        List<String> committeeMemberIds = dto.committeeMembers();
        Set<Professor> committeeMembers = new HashSet<>();

        for (String id : committeeMemberIds) {
            professorRepository.findById(id).ifPresent(committeeMembers::add);
        }

        return new Tcc(
                null,
                dto.title(),
                dto.pathFile(),
                dto.author(),
                course,
                dto.defenseDate(),
                dto.language(),
                advisor,
                committeeMembers,
                dto.summary(),
                dto.abstractText(),
                dto.keywords()
        );
    }

    /**
     * Salva o arquivo no diretório de uploads
     *
     * @param file
     * @return O caminho do arquivo salvo
     * @throws IOException
     */
    public String saveFile(MultipartFile file) throws IOException {

        final String LOCATION = "uploads";
        // Verificar se o arquivo é um PDF
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        // Gerar um UUID único para o nome do arquivo
        String uniqueID = UUID.randomUUID().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // Novo nome de arquivo usando o UUID e a extensão original
        String newFileName = uniqueID + fileExtension;

        // Diretório onde o arquivo será salvo
        Path fileStorageLocation = Paths.get(LOCATION).toAbsolutePath().normalize();
        Files.createDirectories(fileStorageLocation);

        // Localização final do arquivo
        Path targetLocation = fileStorageLocation.resolve(newFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return targetLocation.toString();
    }

    public Tcc saveTcc(Tcc tcc) {
        return tccRepository.save(tcc);
    }

    public Optional<Tcc> findById(String id) {
        return tccRepository.findById(id);
    }

    public List<Tcc> findAll() {
        return tccRepository.findAll();
    }

    public void deleteTcc(String id) {
        findById(id).ifPresent(tcc -> removeFile(tcc.getPathFile()));
        tccRepository.deleteById(id);
    }

    public void removeFile(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean existsById(String id) {
        return tccRepository.existsById(id);
    }

    public TccResponseDTO convertToResponseDTO(Tcc tcc) {
        return new TccResponseDTO(
                tcc.getId(),
                tcc.getTitle(),
                tcc.getAuthor(),
                new CourseResponseDTO(tcc.getCourse()),
                tcc.getDefenseDate(),
                tcc.getLanguage(),
                new ProfessorResponseDTO(tcc.getAdvisor()),
                tcc.getCommitteeMembers().stream().map(ProfessorResponseDTO::new).toList(),
                tcc.getSummary(),
                tcc.getAbstractText(),
                tcc.getKeywords(),
                tcc.getPathFile()
        );
    }
}
