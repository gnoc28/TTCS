package com.example.ttcs.repository;

import com.example.ttcs.entity.HocSinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HocSinhRepository extends JpaRepository<HocSinh, Integer> {
}