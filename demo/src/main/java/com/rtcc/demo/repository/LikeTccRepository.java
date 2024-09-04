package com.rtcc.demo.repository;

import com.rtcc.demo.model.Academic;
import com.rtcc.demo.model.LikeTcc;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.model.id.LikeTccId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface LikeTccRepository extends JpaRepository<LikeTcc, LikeTccId> {
    @Query(value = "SELECT l.* FROM academic_likes_tcc l " +
            "WHERE l.academic_id = :academicId", nativeQuery = true)
    List<LikeTcc> findLikedTccsByAcademicId(@Param("academicId") String academicId);
}
