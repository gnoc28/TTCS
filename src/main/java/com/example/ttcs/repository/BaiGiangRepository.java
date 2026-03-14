package com.example.ttcs.repository;

import com.example.ttcs.entity.BaiGiang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiGiangRepository extends JpaRepository<BaiGiang, Integer> {
}