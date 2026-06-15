package com.example.ttcs.repository;

import com.example.ttcs.entity.BaiGiang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BaiGiangRepository extends JpaRepository<BaiGiang, Integer> {
    // Tìm toàn bộ bài giảng do 1 giáo viên tạo
    List<BaiGiang> findByGiaoVienId(Integer idGiaoVien);
}