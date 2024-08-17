package com.rtcc.demo.controller;


import com.rtcc.demo.DTOs.FilterDTO;
import com.rtcc.demo.DTOs.TccRequestDTO;
import com.rtcc.demo.DTOs.TccResponseDTO;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.services.TccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Collectors;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tcc")
public class TccController {

    @Autowired
    private TccService tccService;


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

            // EXTRAÇÃO DE DE DADOS DOS MEMBROS DA BANCA
            List<Map<String, String>> committeeMembersList = (List<Map<String, String>>) tccMappedData.get("committeeMembers");

            List<String> committeeMembers = (
                    committeeMembersList.stream()
                            .map(member -> member.get("id"))
                            .collect(Collectors.toList())
            );

            // EXTRAÇÃO DE DADOS DAS PALAVRAS-CHAVE
            List<Map<String, String>> keywordsList = (List<Map<String, String>>) tccMappedData.get("keywords");

            List<String> keywords = (
                    keywordsList.stream()
                            .map(member -> member.get("name"))
                            .collect(Collectors.toList())
            );


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

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> tccMappedData = mapper.readValue(tccData, Map.class);

            LocalDate defenseDate = LocalDate.parse((String) tccMappedData.get("defenseDate"));

            List<Map<String, String>> committeeMembersList = (List<Map<String, String>>) tccMappedData.get("committeeMembers");

            List<String> committeeMembers = (
                    committeeMembersList.stream()
                            .map(member -> member.get("id"))
                            .collect(Collectors.toList())
            );

            // EXTRAÇÃO DE DADOS DAS PALAVRAS-CHAVE
            List<Map<String, String>> keywordsList = (List<Map<String, String>>) tccMappedData.get("keywordsList");

            List<String> keywords = (
                    keywordsList.stream()
                            .map(member -> member.get("name"))
                            .collect(Collectors.toList())
            );

            TccRequestDTO updatedTccRequestDTO = new TccRequestDTO(
                    tccMappedData.get("title").toString(),
                    tccMappedData.get("author").toString(),
                    (String) ((Map<String, Object>) tccMappedData.get("course")).get("id"),
                    defenseDate,
                    tccMappedData.get("language").toString(),
                    (String) ((Map<String, Object>) tccMappedData.get("advisor")).get("id"),
                    committeeMembers,
                    tccMappedData.get("summary").toString(),
                    tccMappedData.get("abstractText").toString(),
                    keywords,
                    tccMappedData.get("pathFile").toString()
            );

            if (!tccService.dtoIsValid(updatedTccRequestDTO))
                return ResponseEntity.badRequest().build();

            if (existingTcc.isEmpty())
                return ResponseEntity.notFound().build();

            Tcc tcc = tccService.convertDtoToEntity(updatedTccRequestDTO);

            tcc.setId(id);

            tcc.setPathFile(existingTcc.get().getPathFile());

            if (file != null) {
                tccService.removeFile(tcc.getPathFile());

                tcc.setPathFile(tccService.saveFile(file));
            }

            tcc = tccService.saveTcc(tcc);

            TccResponseDTO tccResponseDTO = tccService.convertToResponseDTO(tcc);

            return ResponseEntity.ok().body(tccResponseDTO);

        } catch (IOException e) {
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


    @GetMapping("/filter")
    public ResponseEntity<List<TccResponseDTO>> filterTccs(@RequestBody FilterDTO filterDTO) {
        String filter = filterDTO.getFilter();
        String value = filterDTO.getValue();
        List<Tcc> tccList = tccService.filterTccs(filter, value);

        List<TccResponseDTO> tccResponseDTOList = tccList.stream()
                .map(tccService::convertToResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(tccResponseDTOList);
    }

}