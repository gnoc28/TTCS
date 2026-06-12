package com.example.ttcs.repository;

import com.example.ttcs.entity.BaiGiangLopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BaiGiangLopHocRepository extends JpaRepository<BaiGiangLopHoc, Integer> {
    // Tìm các bài giảng được gán cho 1 lớp học cụ thể
    List<BaiGiangLopHoc> findByLopHocId(Integer idLopHoc);
    
    void deleteByBaiGiangId(Integer baiGiangId);

    void deleteByLopHocId(Integer lopHocId);
}