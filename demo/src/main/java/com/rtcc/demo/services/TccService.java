package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.CourseResponseDTO;
import com.rtcc.demo.DTOs.ProfessorResponseDTO;
import com.rtcc.demo.DTOs.TccRequestDTO;
import com.rtcc.demo.DTOs.TccResponseDTO;
import com.rtcc.demo.model.Course;
import com.rtcc.demo.model.Keywords;
import com.rtcc.demo.model.Professor;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.repository.CourseRepository;
import com.rtcc.demo.repository.KeywordsRepository;
import com.rtcc.demo.repository.ProfessorRepository;
import com.rtcc.demo.repository.TccRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.rtcc.demo.DTOs.KeywordsResponseDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TccService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private TccRepository tccRepository;

    @Autowired
    private KeywordsRepository keywordsRepository;

    /**
     * Converte um DTO em uma entidade
     *
     * @param dto
     * @return
     */
    public Tcc convertDtoToEntity(TccRequestDTO dto) {
        Course course = courseRepository.findById(dto.course())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Professor advisor = professorRepository.findById(dto.advisor())
                .orElseThrow(() -> new RuntimeException("Advisor not found"));

        List<String> committeeMemberIds = dto.committeeMembers();
        Set<Professor> committeeMembers = new HashSet<>();

        // Verifica se os membros do comitê existem e adiciona à lista
        for (String id : committeeMemberIds) {
            Professor p = professorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Professor not found"));
            committeeMembers.add(p);

        }

        // Verifica se as palavras-chave existem e adiciona à lista
        List<String> keywords = dto.keywords();
        Set<Keywords> keywordsSet = new HashSet<>();

        for (String keyword : keywords) {
            Keywords k = keywordsRepository.findById(keyword).orElseThrow(() -> new RuntimeException("Keyword not found"));
            keywordsSet.add(k);
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
                keywordsSet
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
                tcc.getKeywords().stream().map(keyword -> new KeywordsResponseDTO(keyword.getName())).toList(),
                tcc.getPathFile()
        );
    }


    /**
     * Verifica se os campos obrigatórios do DTO estão preenchidos
     *
     * @param tccRequestDTO
     * @return true se todos os campos obrigatórios estiverem preenchidos, false caso contrário
     */
    public boolean dtoIsValid(TccRequestDTO tccRequestDTO) {
        if (tccRequestDTO.title() == null || tccRequestDTO.author() == null || tccRequestDTO.course() == null
                || tccRequestDTO.defenseDate() == null || tccRequestDTO.language() == null || tccRequestDTO.advisor()
                == null || tccRequestDTO.committeeMembers() == null || tccRequestDTO.summary() == null ||
                tccRequestDTO.abstractText() == null || tccRequestDTO.keywords() == null) {
            return false;
        }
        return true;
    }

    /**
     * Search Tccs by a query string that can contain multiple keywords
     *
     * @param query
     * @return List of Tcc
     */
    public List<Tcc> searchTccs(String query) {
        String[] keywords = query.split("\\+");
        List<Tcc> result = new ArrayList<>();

        for (String keyword : keywords) {
            result.addAll(tccRepository.searchTccs(keyword.trim()));
        }

        return result.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Filter Tccs by a specific field
     *
     * @param filter
     * @param value
     * @return List of Tcc
     */
    public List<Tcc> filterTccs(String filter, String value) {
        switch (filter.toLowerCase()) {
            case "title":
                return tccRepository.searchTccsByTitle(value);
            case "defense_date":
                return tccRepository.searchTccsByDefenseDate(LocalDate.parse(value));
            case "advisor":
                return tccRepository.searchTccsByAdvisor(value);
            case "author":
                return tccRepository.searchTccsByAuthor(value);
            case "course":
                return tccRepository.searchTccsByCourse(value);
            case "keywords":
                return tccRepository.searchTccsByKeywords(value);
            default:
                throw new IllegalArgumentException("Filtro inválido: " + filter);
        }

    }
}
