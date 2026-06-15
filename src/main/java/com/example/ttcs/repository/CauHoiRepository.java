package com.example.ttcs.repository;

import com.example.ttcs.entity.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CauHoiRepository extends JpaRepository<CauHoi, Integer> {
    List<CauHoi> findByDeIdOrderByThuTuAsc(Integer deId);

    void deleteByDeId(Integer deId);

}