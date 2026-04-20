package com.example.ttcs.repository;

import com.example.ttcs.entity.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonHocRepository extends JpaRepository<MonHoc, Integer> {
    List<MonHoc> findByKhoiLopId(Integer khoiLopId);
}