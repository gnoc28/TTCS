package com.example.ttcs.repository;

import com.example.ttcs.entity.GiaoVienMonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GiaoVienMonHocRepository extends JpaRepository<GiaoVienMonHoc, Integer> {
    List<GiaoVienMonHoc> findByGiaoVienId(Integer giaoVienId);

    void deleteByGiaoVienId(Integer giaoVienId);
}