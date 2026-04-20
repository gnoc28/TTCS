package com.example.ttcs.repository;

import com.example.ttcs.entity.KhoiLop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoiLopRepository extends JpaRepository<KhoiLop, Integer> {
}