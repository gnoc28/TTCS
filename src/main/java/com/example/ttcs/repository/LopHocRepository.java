package com.example.ttcs.repository;

import com.example.ttcs.entity.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, Integer> {
    List<LopHoc> findByGiaoVien_NguoiDung_TenDangNhap(String tenDangNhap);

}