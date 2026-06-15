package com.example.ttcs.repository;

import com.example.ttcs.entity.Truong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface TruongRepository extends JpaRepository<Truong, Integer> {
    Optional<Truong> findByTen(String ten);

    Optional<Truong> findByTenAndTinhAndXa(String ten, String tinh, String xa);

    @Query("SELECT t FROM Truong t WHERE LOWER(t.ten) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Truong> searchByTen(@Param("keyword") String keyword);
}