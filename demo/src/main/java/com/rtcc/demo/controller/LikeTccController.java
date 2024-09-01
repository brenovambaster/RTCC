package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.LikeTccRequestDTO;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.services.LikeTccService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/tcc")
@Tag(name = "Like Tcc")
public class LikeTccController {

    private final LikeTccService likeTccService;

    public LikeTccController(LikeTccService likeTccService) {
        this.likeTccService = likeTccService;
    }


    @PostMapping("/like")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> likeTcc(@RequestBody LikeTccRequestDTO dto) {

        if (likeTccService.likeTcc(dto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/unlike")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> unlikeTcc(@RequestBody LikeTccRequestDTO dto) {

        if (likeTccService.unlikeTcc(dto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/likes")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<Tcc>> getLikesByAcademic(@RequestParam String academicId) {
        return ResponseEntity.ok(likeTccService.getLikesByAcademic(academicId));
    }
}
