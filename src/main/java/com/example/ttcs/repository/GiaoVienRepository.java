package com.example.ttcs.repository;

import com.example.ttcs.entity.GiaoVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiaoVienRepository extends JpaRepository<GiaoVien, Integer> {
}