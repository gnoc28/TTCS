package com.example.ttcs.repository;

import com.example.ttcs.entity.HocSinhLop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HocSinhLopRepository extends JpaRepository<HocSinhLop, Integer> {

    // Kiểm tra xem học sinh đã có trong lớp chưa
    boolean existsByLopHocIdAndHocSinhId(Integer lopHocId, Integer hocSinhId);

    // Tìm bản ghi học sinh - lớp học để xóa
    Optional<HocSinhLop> findByLopHocIdAndHocSinhId(Integer lopHocId, Integer hocSinhId);

    // THÊM HÀM NÀY: Để lấy toàn bộ danh sách học sinh đổ ra bảng React
    List<HocSinhLop> findByLopHocId(Integer lopHocId);

    List<HocSinhLop> findByHocSinhId(Integer hocSinhId);
}