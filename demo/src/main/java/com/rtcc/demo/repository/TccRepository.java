package com.rtcc.demo.repository;

import com.rtcc.demo.model.Professor;
import com.rtcc.demo.model.Tcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TccRepository extends JpaRepository<Tcc, String> {

    @Query(value = "SELECT tcc.* FROM tcc " +
            "JOIN professor ON tcc.advisor = professor.id " +
            "JOIN course ON tcc.course = course.id " +
            "WHERE LOWER(tcc.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(tcc.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(professor.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(course.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(tcc.keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    List<Tcc> searchTccs(@Param("keyword") String keyword);


    @Query("SELECT t FROM Tcc t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Tcc> searchTccsByTitle(@Param("value") String value);

    @Query("SELECT t FROM Tcc t WHERE t.defenseDate = :value")
    List<Tcc> searchTccsByDefenseDate(@Param("value") LocalDate value);

    @Query("SELECT t FROM Tcc t WHERE LOWER(t.advisor) = :value")
    List<Tcc> searchTccsByAdvisor(@Param("value") String value);

    @Query("SELECT t FROM Tcc t WHERE LOWER(t.author) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Tcc> searchTccsByAuthor(@Param("value") String value);

    @Query("SELECT t FROM Tcc t WHERE LOWER(t.course) = :value")
    List<Tcc> searchTccsByCourse(@Param("value") String courseId);

    @Query("SELECT t FROM Tcc t WHERE LOWER(t.keywords) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Tcc> searchTccsByKeywords(@Param("value") String value);

    boolean existsByCourseId(String id);

    boolean existsByAdvisorId(String id);

    boolean existsByCommitteeMembersId(String id);

    @Query("SELECT t FROM Tcc t WHERE EXTRACT(YEAR FROM t.defenseDate) = :year AND EXTRACT(MONTH FROM t.defenseDate) = :month")
    List<Tcc> searchTccsByMonthYear(@Param("year") int year, @Param("month") int month);

    @Query("SELECT t FROM Tcc t WHERE EXTRACT(YEAR FROM t.defenseDate) = :year")
    List<Tcc> searchTccsByYear(@Param("year") int year);
}
