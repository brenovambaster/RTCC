package com.rtcc.demo.repository;

import com.rtcc.demo.model.Academic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademicRepository extends JpaRepository<Academic, String> {
}
