package com.rtcc.demo.repository;

import com.rtcc.demo.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, String> {

}
