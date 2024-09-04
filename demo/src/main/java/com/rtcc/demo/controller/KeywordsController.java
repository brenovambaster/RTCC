package com.rtcc.demo.controller;

import com.rtcc.demo.DTOs.KeywordsRequestDTO;
import com.rtcc.demo.exception.EntityAlreadyExistsException;
import com.rtcc.demo.exception.EntityDeletionException;
import com.rtcc.demo.model.Keywords;
import com.rtcc.demo.repository.KeywordsRepository;
import com.rtcc.demo.repository.TccRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/keywords")
public class KeywordsController {
    @Autowired
    private KeywordsRepository keywordsRepository;

    @PostMapping
    public ResponseEntity<Void> saveKeywords(@RequestBody KeywordsRequestDTO data) {
        keywordsRepository.findById(data.name()).ifPresent(keywords -> {
            throw new EntityAlreadyExistsException("Palavra-Chave");
        });

        Keywords keywords = new Keywords(data.name());
        keywordsRepository.save(keywords);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Keywords>> getKeywords() {
        return ResponseEntity.ok(keywordsRepository.findAll());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteKeywords(@PathVariable String name) {

        keywordsRepository.findById(name).orElseThrow(() -> new RuntimeException("Keywords not found"));

        try {
            keywordsRepository.deleteById(name);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDeletionException("Palavra-Chave", "Essa palavra chave está sendo usada em um ou mais TCC");
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Keywords> getKeywordsByName(@PathVariable String name) {
        return ResponseEntity.ok(keywordsRepository.findById(name).get());
    }
}
