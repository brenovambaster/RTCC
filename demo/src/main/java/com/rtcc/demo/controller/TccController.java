package com.rtcc.demo.controller;


import com.rtcc.demo.DTOs.FilterDTO;
import com.rtcc.demo.DTOs.TccRequestDTO;
import com.rtcc.demo.DTOs.TccResponseDTO;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.repository.TccRepository;
import com.rtcc.demo.services.TccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tcc")
public class TccController {

    @Autowired
    private TccService tccService;
    @Autowired
    private TccRepository tccRepository;


    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(
            summary = "Create a new TCC",
            parameters = {
                    @Parameter(name = "file", description = "PDF file of the TCC", required = true),
                    @Parameter(name = "tccData", description = "Data of the TCC", required = true)
            },
            description = "Add a new TCC into the database",
            responses = {
                    @ApiResponse(responseCode = "201", description = "TCC saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<TccResponseDTO> createTcc(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("tccData") String tccData) {

        try {
            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(null);
            }

            String filePath = tccService.saveFile(file);

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> tccMappedData = mapper.readValue(tccData, Map.class);

            LocalDate defenseDate = LocalDate.parse((String) tccMappedData.get("defenseDate"));

            List<String> committeeMembers = tccService.extractCommitteeMembers(tccMappedData);

            List<String> keywords = tccService.extractKeywords(tccMappedData);

            // SALVAR PALAVRAS-CHAVE NO BANCO DE DADOS, CASO ALGUMA DELAS NÃO EXISTAM
            tccService.saveKeywordsIfNotExists(keywords);

            TccRequestDTO updatedTccRequestDTO = new TccRequestDTO(
                    tccMappedData.get("title").toString(),
                    tccMappedData.get("author").toString(),
                    tccMappedData.get("course").toString(),
                    defenseDate,
                    tccMappedData.get("language").toString(),
                    (String) ((Map<String, Object>) tccMappedData.get("advisor")).get("id"),
                    committeeMembers,
                    tccMappedData.get("summary").toString(),
                    tccMappedData.get("abstractText").toString(),
                    keywords,
                    filePath
            );

            Tcc tcc = tccService.convertDtoToEntity(updatedTccRequestDTO);
            Tcc savedTcc = tccService.saveTcc(tcc);

            TccResponseDTO responseDTO = tccService.convertToResponseDTO(savedTcc);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(
            summary = "Get TCC by ID",
            description = "Get a TCC by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "404", description = "TCC not found")
            }
    )
    public ResponseEntity<Tcc> getTccById(@PathVariable String id) {
        Optional<Tcc> tcc = tccService.findById(id);
        return tcc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(
            summary = "Get all TCCs",
            description = "Get all TCCs stored in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success")
            }
    )
    public ResponseEntity<List<TccResponseDTO>> getAllTccs() {
        List<Tcc> tccList = tccService.findAll();

        List<TccResponseDTO> tccResponseDTOList = tccList.stream().map(tccService::convertToResponseDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(tccResponseDTOList);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(
            summary = "Update TCC by ID",
            description = "Update a TCC by its ID, if it exists. Otherwise, return 404 Not Found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "TCC updated successfully"),
                    @ApiResponse(responseCode = "404", description = "TCC not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "415", description = "Unsupported media type"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<TccResponseDTO> updateTcc(@PathVariable String id,
                                                    MultipartFile file,
                                                    @RequestParam("tccData") String tccData) {

        try {

            Optional<Tcc> existingTcc = tccService.findById(id);
            TccRequestDTO updatedTccRequestDTO = tccService.convertJsonToTccRequestDTO(tccData);


            if (!tccService.dtoIsValid(updatedTccRequestDTO))
                return ResponseEntity.badRequest().build();

            if (existingTcc.isEmpty())
                return ResponseEntity.notFound().build();

            Tcc tcc = tccService.convertDtoToEntity(updatedTccRequestDTO);

            tcc.setId(id);
            tcc.setNumLikes(existingTcc.get().getNumLikes());
            tcc.setNumFavorites(existingTcc.get().getNumFavorites());

            tcc.setPathFile(existingTcc.get().getPathFile());

            if (file != null) {
                tccService.removeFile(tcc.getPathFile());

                tcc.setPathFile(tccService.saveFile(file));
            }

            tcc = tccService.saveTcc(tcc);

            TccResponseDTO tccResponseDTO = tccService.convertToResponseDTO(tcc);

            return ResponseEntity.ok().body(tccResponseDTO);


        } catch (IOException e) {
            System.out.println("\n\nMensagem de erro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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


    @GetMapping("/search")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(
            summary = "Search TCCs",
            description = "Search for TCCs based on the provided query",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success")
            }
    )
    public ResponseEntity<List<TccResponseDTO>> searchTccs(@RequestParam String query) {
        List<Tcc> tccList = tccService.searchTccs(query);

        List<TccResponseDTO> tccResponseDTOList = tccList.stream()
                .map(tccService::convertToResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(tccResponseDTOList);
    }


    @PostMapping("/filter")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<TccResponseDTO>> filterTccs(@RequestBody FilterDTO filterDTO) {
        String filter = filterDTO.getFilter();
        String value = filterDTO.getValue();
        List<Tcc> tccList = tccService.filterTccs(filter, value);

        List<TccResponseDTO> tccResponseDTOList = tccList.stream()
                .map(tccService::convertToResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(tccResponseDTOList);
    }

    @GetMapping("/view/{filename}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        try {
            Path filePath = tccService.getFilePath(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/like/most-liked")
    public ResponseEntity<List<TccResponseDTO>> getMostTenLikedTccs() {
        List<Tcc> tccList = tccRepository.mostTenLikedTccs();
        List<TccResponseDTO> tccResponseDTOList =
                tccList.stream().map(tccService::convertToResponseDTO).toList();

        return ResponseEntity.status(HttpStatus.OK).body(tccResponseDTOList);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/favorite/most-favorited")
    public ResponseEntity<List<TccResponseDTO>> getMostTenFavoritedTccs() {
        List<Tcc> tccList = tccRepository.mostTenFavoritedTccs();
        List<TccResponseDTO> tccResponseDTOList =
                tccList.stream().map(tccService::convertToResponseDTO).toList();

        return ResponseEntity.status(HttpStatus.OK).body(tccResponseDTOList);
    }

}
