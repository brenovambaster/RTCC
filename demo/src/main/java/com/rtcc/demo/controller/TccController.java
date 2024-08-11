package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.TccRequestDTO;
import com.rtcc.demo.DTOs.TccResponseDTO;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.service.TccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.io.IOException;
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
    @Operation(summary = "Create a new TCC", parameters = {@Parameter(name = "file", description = "PDF file of the TCC", required = true), @Parameter(name = "tccData", description = "Data of the TCC", required = true)}, description = "Add a new TCC into the database", responses = {@ApiResponse(responseCode = "201", description = "TCC saved successfully"), @ApiResponse(responseCode = "400", description = "Bad request"), @ApiResponse(responseCode = "415", description = "Unsupported Media Type"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    public ResponseEntity<TccResponseDTO> createTcc(@RequestPart("file") MultipartFile file, @RequestPart("tccData") @Valid TccRequestDTO tccRequestDTO) {

        try {

            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(null);
            }

            String filePath = tccService.saveFile(file);

            // Atualiza o DTO com o caminho do arquivo
            TccRequestDTO updatedTccRequestDTO = new TccRequestDTO(tccRequestDTO.title(), tccRequestDTO.author(), tccRequestDTO.course(), tccRequestDTO.defenseDate(), tccRequestDTO.language(), tccRequestDTO.advisor(), tccRequestDTO.committeeMembers(), tccRequestDTO.summary(), tccRequestDTO.abstractText(), tccRequestDTO.keywords(), filePath);

            // Converte para entidade e salva
            Tcc tcc = tccService.convertDtoToEntity(updatedTccRequestDTO);
            Tcc savedTcc = tccService.saveTcc(tcc);

            // Cria o DTO de resposta
            TccResponseDTO responseDTO = tccService.convertToResponseDTO(savedTcc);

            // Retorna a resposta com status 201 e o cabeçalho Location
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            // Log do erro pode ser adicionado aqui
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(summary = "Get TCC by ID", description = "Get a TCC by its ID, if it exists. Otherwise, return 404 Not Found.", responses = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "TCC not found")})
    public ResponseEntity<Tcc> getTccById(@PathVariable String id) {
        Optional<Tcc> tcc = tccService.findById(id);
        return tcc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(summary = "Get all TCCs", description = "Get all TCCs stored in the database", responses = {@ApiResponse(responseCode = "200", description = "Success")

    })
    public ResponseEntity<List<TccResponseDTO>> getAllTccs() {
        List<Tcc> tccList = tccService.findAll();

        List<TccResponseDTO> tccResponseDTOList = tccList.stream().map(tccService::convertToResponseDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(tccResponseDTOList);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(summary = "Update TCC by ID",
            description = "Update a TCC by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "TCC updated successfully"),
                    @ApiResponse(responseCode = "404", description = "TCC not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "415", description = "Unsupported media type"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")})
    public ResponseEntity<Tcc> updateTcc(@PathVariable String id,
                                         @RequestPart(value = "file") MultipartFile file,
                                         @RequestPart(value = "tccData") @Valid TccRequestDTO tccRequestDTO) {

        Optional<Tcc> existingTcc = tccService.findById(id);

        if (!tccService.dtoIsValid(tccRequestDTO))
            return ResponseEntity.badRequest().build();

        if (existingTcc.isEmpty())
            return ResponseEntity.notFound().build();
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        Tcc tcc = tccService.convertDtoToEntity(tccRequestDTO);
        tcc.setId(id);
        tcc.setPathFile(existingTcc.get().getPathFile());

        try {
            tccService.removeFile(tcc.getPathFile()); // remover o arquivo antigo
            tcc.setPathFile(tccService.saveFile(file)); // salvar o novo arquivo
            tcc = tccService.saveTcc(tcc);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().body(tcc);

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
