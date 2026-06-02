package com.example.ttcs.repository;

import com.example.ttcs.entity.KetQua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface KetQuaRepository extends JpaRepository<KetQua, Integer> {
    Optional<KetQua> findTopByHocSinhIdAndDeIdOrderByLanThuDesc(Integer hocSinhId, Integer deId);
    
    List<KetQua> findByDeId(Integer deId);

    List<KetQua> findByHocSinhIdAndDeIdOrderByLanThuAsc(Integer hocSinhId, Integer deId);

    void deleteByDeId(Integer deId);
}