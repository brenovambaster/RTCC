package com.rtcc.demo.repository;

import com.rtcc.demo.model.FavoriteTcc;
import com.rtcc.demo.model.id.FavoriteTccId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteTccRepository extends JpaRepository<FavoriteTcc, FavoriteTccId> {
    @Query(value = "SELECT l.* FROM public.academic_favorites l " +
            "WHERE l.academic_id = :academicId", nativeQuery = true)
    List<FavoriteTcc> findFavoritesByAcademicId(@Param("academicId") String academicId);

}
