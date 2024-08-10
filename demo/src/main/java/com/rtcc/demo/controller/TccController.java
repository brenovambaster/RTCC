package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.TccRequestDTO;
import com.rtcc.demo.DTOs.TccResponseDTO;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.service.TccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tcc")
public class TccController {

    private final TccService tccService;

    @Autowired
    public TccController(TccService tccService) {
        this.tccService = tccService;
    }

    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<TccResponseDTO> createTcc(
            @RequestPart("file") MultipartFile file,
            @RequestPart("tccData") @Valid TccRequestDTO tccRequestDTO) {

        try {

            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(null);
            }

            String filePath = tccService.saveFile(file);

            // Atualiza o DTO com o caminho do arquivo
            TccRequestDTO updatedTccRequestDTO = new TccRequestDTO(
                    tccRequestDTO.title(),
                    tccRequestDTO.author(),
                    tccRequestDTO.course(),
                    tccRequestDTO.defenseDate(),
                    tccRequestDTO.language(),
                    tccRequestDTO.advisor(),
                    tccRequestDTO.committeeMembers(),
                    tccRequestDTO.summary(),
                    tccRequestDTO.abstractText(),
                    tccRequestDTO.keywords(),
                    filePath
            );

            // Converte para entidade e salva
            Tcc tcc = tccService.convertToEntity(updatedTccRequestDTO);
            Tcc savedTcc = tccService.saveTcc(tcc);

            // Cria o DTO de resposta
            TccResponseDTO responseDTO = tccService.convertToResponseDTO(savedTcc);

            // Retorna a resposta com status 201 e o cabeçalho Location
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            // Log do erro pode ser adicionado aqui
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tcc> getTccById(@PathVariable String id) {
        Optional<Tcc> tcc = tccService.findById(id);
        return tcc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<TccResponseDTO>> getAllTccs() {
        List<Tcc> tccList = tccService.findAll();

        List<TccResponseDTO> tccResponseDTOList = tccList.stream()
                .map(tccService::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(tccResponseDTOList);
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Tcc> updateTcc(@PathVariable String id, @RequestBody @Valid TccRequestDTO tccRequestDTO) {
        Optional<Tcc> existingTcc = tccService.findById(id);
        if (existingTcc.isPresent()) {
            Tcc tcc = tccService.convertToEntity(tccRequestDTO);
            tcc.setId(id);  // Ensure the ID is set for the update
            Tcc updatedTcc = tccService.saveTcc(tcc);
            return ResponseEntity.ok(updatedTcc);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> deleteTcc(@PathVariable String id) {
        if (tccService.existsById(id)) {
            tccService.deleteTcc(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
