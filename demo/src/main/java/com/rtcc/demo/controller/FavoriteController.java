package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.FavoriteTccRequestDTO;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.services.FavoriteTccService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tcc/favorites")
@Tag(name = "Favorite Tcc")
public class FavoriteController {

    private final FavoriteTccService favoriteTccService;

    @Autowired
    public FavoriteController(FavoriteTccService favoriteTccService) {
        this.favoriteTccService = favoriteTccService;
    }

    /**
     * Endpoint para favoritar um TCC
     *
     * @param dto Favoritar TCC request DTO
     * @return ResponseEntity com status de sucesso ou erro
     */
    @PostMapping("/add")
    public ResponseEntity<Void> favoriteTcc(@RequestBody FavoriteTccRequestDTO dto) {
        boolean success = favoriteTccService.favoriteTcc(dto);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para desfavoritar um TCC
     *
     * @param dto Desfavoritar TCC request DTO
     * @return ResponseEntity com status de sucesso ou erro
     */
    @PostMapping("/remove")
    public ResponseEntity<Void> unfavoriteTcc(@RequestBody FavoriteTccRequestDTO dto) {
        boolean success = favoriteTccService.unfavoriteTcc(dto);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para obter todos os TCCs favoritados por um acadêmico
     *
     * @param academicId ID do acadêmico
     * @return ResponseEntity com a lista de TCCs favoritados
     */
    @GetMapping("/by-academic")
    public ResponseEntity<List<Tcc>> getTccFavoritesByAcademic(@RequestParam String academicId) {
        List<Tcc> favorites = favoriteTccService.getTccFavoritesByAcademic(academicId);
        return ResponseEntity.ok(favorites);
    }
}
