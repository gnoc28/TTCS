package com.example.ttcs.repository;

import com.example.ttcs.entity.KetQua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaRepository extends JpaRepository<KetQua, Integer> {
}