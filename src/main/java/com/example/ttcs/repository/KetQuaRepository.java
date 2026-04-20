package com.example.ttcs.repository;

import com.example.ttcs.entity.KetQua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KetQuaRepository extends JpaRepository<KetQua, Integer> {
    Optional<KetQua> findTopByHocSinhIdAndDeIdOrderByLanThuDesc(Integer hocSinhId, Integer deId);
}