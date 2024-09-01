package com.rtcc.demo.repository;

import com.rtcc.demo.model.Academic;
import com.rtcc.demo.model.Tcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface AcademicRepository extends JpaRepository<Academic, String> {
    @Query(value = "SELECT t.* FROM tcc t " +
            "JOIN academic_likes_tcc alt ON t.id = alt.tcc_id " +
            "WHERE alt.academic_id = :academicId",
            nativeQuery = true)
    Set<Tcc> findLikedTccsByAcademicId(@Param("academicId") String academicId);
}
