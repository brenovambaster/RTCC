package com.rtcc.demo.repository;

import com.rtcc.demo.model.Professor;
import com.rtcc.demo.model.Tcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TccRepository extends JpaRepository<Tcc, String> {

    @Query(value = "SELECT * FROM tcc " +
            "WHERE LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(advisor) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(course) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    List<Tcc> searchTccs(@Param("keyword") String keyword);


//    @Query(value = "SELECT * FROM tcc " +
//            "WHERE LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(advisor) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(course) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))",
//            nativeQuery = true)
//    List<Tcc> searchTccs(String filter, String value);



}
