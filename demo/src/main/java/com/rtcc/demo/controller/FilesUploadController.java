package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.FilesRequestDTO;
import com.rtcc.demo.DTOs.FilesResponseDTO;
import com.rtcc.demo.DTOs.ProfessorRequestDTO;
import com.rtcc.demo.DTOs.ProfessorResponseDTO;
import com.rtcc.demo.model.Files;
import com.rtcc.demo.model.Professor;
import com.rtcc.demo.repository.FileRepository;
import com.rtcc.demo.storage.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FilesUploadController {

    @Autowired
    private FilesStorageService fileStorageService;

    @Autowired
    private FileRepository fileRepository;

    @PostMapping("/upload")
    public ResponseEntity<FilesResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Salvar arquivo no diretório e obter o nome único
            String uniqueNewFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            fileStorageService.saveFile(file,uniqueNewFileName);

            // Salvar metadados no banco de dados
            Files fileEntity = Files.builder()
                    .name(uniqueNewFileName)
                    .type(file.getContentType())
                    .path(fileStorageService.getFileStorageLocation().toString())
                    .build();
            fileRepository.save(fileEntity);

//            // Criar resposta
            FilesResponseDTO response = new FilesResponseDTO(
                    fileEntity.getId(),
                    fileEntity.getName(),
                    fileEntity.getType(),
                    fileEntity.getPath()
            );

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/allFiles")
    public ResponseEntity<List<FilesResponseDTO>> getAllFilesFromDB() {
        List<FilesResponseDTO> files = fileRepository
                .findAll()
                .stream()
                .map(FilesResponseDTO::new)
                .toList();

        if (files.isEmpty()) {
            return ResponseEntity.notFound().build();  // Retorna 404 se a lista estiver vazia
        }

        return ResponseEntity.ok(files);  // Retorna 200 e a lista de arquivos se houver resultados
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<FilesResponseDTO> getFile(@PathVariable String id) {
        Optional<Files> files = fileRepository.findById(id);

        if (files.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        FilesResponseDTO filesResponse = new FilesResponseDTO(files.get());
        return ResponseEntity.ok().body(filesResponse);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<FilesResponseDTO> updateFile(@PathVariable String id,
                                                       @RequestParam String name) throws IOException {
//        @RequestBody -> nao é necessário pois estamos atualizando apenas o nome

        Optional<Files> fileOptional = fileRepository.findById(id);

        if (fileOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Files file = fileOptional.get();

        String uniqueNewFileName = UUID.randomUUID() + "_" + name;
        // Verifica se a extensão .pdf já está presente, caso contrário, adiciona
        if (!uniqueNewFileName.toLowerCase().endsWith(".pdf")) {
            uniqueNewFileName += ".pdf";
        }
        fileStorageService.renameFile(file.getName(), uniqueNewFileName);

        file.setName(uniqueNewFileName);

        // Não é necessário atualizar o path e o tipo
        // file.setPath(fileOptional.get().getPath());
        // file.setType(fileOptional.get().getType());

        fileRepository.save(file);

        FilesResponseDTO fileResponse = new FilesResponseDTO(file);
        return ResponseEntity.ok().body(fileResponse);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        if (!fileRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        //obter o nome do arquivo atraves do fileRepository ID
        Optional<Files> fileOptional = fileRepository.findById(id);
        Files file = fileOptional.get();
        String pdfName = file.getName();

        fileStorageService.deleteByName(pdfName);
        fileRepository.deleteById(id);

        //Retorna OK para deleção ou NO CONTENT ou body deleted?
        return ResponseEntity.ok().build();
    }

}
