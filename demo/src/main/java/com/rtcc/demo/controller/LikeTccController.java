package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.LikeTccRequestDTO;
import com.rtcc.demo.DTOs.TccResponseDTO;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.services.LikeTccService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tcc/like")
@Tag(name = "Like Tcc")
public class LikeTccController {

    private final LikeTccService likeTccService;

    public LikeTccController(LikeTccService likeTccService) {
        this.likeTccService = likeTccService;
    }

    /**
     * Endpoint para curtir um TCC
     *
     * @param dto LikeTccRequestDTO
     * @return ResponseEntity com status de sucesso ou erro
     */
    @PostMapping("/add")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> likeTcc(@RequestBody LikeTccRequestDTO dto) {
        boolean success = likeTccService.likeTcc(dto);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para remover curtida de um TCC
     *
     * @param dto LikeTccRequestDTO
     * @return ResponseEntity com status de sucesso ou erro
     */
    @PostMapping("/remove")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> unlikeTcc(@RequestBody LikeTccRequestDTO dto) {
        boolean success = likeTccService.unlikeTcc(dto);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para obter todos os TCCs curtidos por um acadêmico
     *
     * @param academicId ID do acadêmico
     * @return ResponseEntity com a lista de TCCs curtidos
     */
    @GetMapping("/by-academic")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<Tcc>> getLikesByAcademic(@RequestParam String academicId) {
        List<Tcc> likedTccs = likeTccService.getTccLikesByAcademic(academicId);
        return ResponseEntity.ok(likedTccs);
    }

    @PostMapping("/get-liked-tcc")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<TccResponseDTO> getLikedTcc(@RequestBody LikeTccRequestDTO dto) {
        return ResponseEntity.ok(likeTccService.getLikedTcc(dto));
    }
}
