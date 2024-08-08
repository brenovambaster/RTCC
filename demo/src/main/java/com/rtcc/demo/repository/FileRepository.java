package com.rtcc.demo.repository;

import com.rtcc.demo.model.Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Files, String> {

//    Files findByName(String name);
}