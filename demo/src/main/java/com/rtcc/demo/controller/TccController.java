package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.TccRequestDTO;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.service.TccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tcc")
public class TccController {

    @Autowired
    private TccService tccService;

    @PostMapping
    public ResponseEntity<Tcc> createTcc(@RequestBody TccRequestDTO tccRequestDTO) {
        Tcc tcc = tccService.convertToEntity(tccRequestDTO);
        Tcc savedTcc = tccService.saveTcc(tcc);
        return new ResponseEntity<>(savedTcc, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tcc> getTccById(@PathVariable String id) {
        Optional<Tcc> tcc = tccService.findById(id);
        return tcc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Tcc>> getAllTccs() {
        List<Tcc> tccList = tccService.findAll();
        return ResponseEntity.ok(tccList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tcc> updateTcc(@PathVariable String id, @RequestBody TccRequestDTO tccRequestDTO) {
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
    public ResponseEntity<Void> deleteTcc(@PathVariable String id) {
        if (tccService.existsById(id)) {
            tccService.deleteTcc(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
