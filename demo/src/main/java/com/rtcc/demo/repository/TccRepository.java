package com.rtcc.demo.repository;

import com.rtcc.demo.model.Professor;
import com.rtcc.demo.model.Tcc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TccRepository extends JpaRepository<Tcc, String> {

}
