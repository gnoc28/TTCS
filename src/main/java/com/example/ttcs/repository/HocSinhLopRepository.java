package com.example.ttcs.repository;

import com.example.ttcs.entity.HocSinhLop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HocSinhLopRepository extends JpaRepository<HocSinhLop, Integer> {
}