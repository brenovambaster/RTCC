package com.rtcc.demo.repository;

import com.rtcc.demo.model.Keywords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordsRepository extends JpaRepository<Keywords, String> {
}
