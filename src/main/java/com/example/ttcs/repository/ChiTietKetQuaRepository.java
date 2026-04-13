package com.example.ttcs.repository;

import com.example.ttcs.entity.ChiTietKetQua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietKetQuaRepository extends JpaRepository<ChiTietKetQua, Integer> {
    List<ChiTietKetQua> findByKetQuaId(Integer ketQuaId);
}