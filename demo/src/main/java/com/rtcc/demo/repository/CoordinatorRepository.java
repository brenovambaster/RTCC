package com.rtcc.demo.repository;

import com.rtcc.demo.model.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatorRepository extends JpaRepository<Coordinator, String> {
    boolean existsByCourseId(String id);
}
