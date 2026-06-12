package com.example.ttcs.repository;

import com.example.ttcs.entity.HocSinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface HocSinhRepository extends JpaRepository<HocSinh, Integer> {
    Optional<HocSinh> findByMaHS(String maHS);
    
    List<HocSinh> findByMaHSContainingIgnoreCaseOrNguoiDung_TenContainingIgnoreCase(
            String maHS, String ten);
}