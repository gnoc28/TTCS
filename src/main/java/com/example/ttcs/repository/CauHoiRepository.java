package com.example.ttcs.repository;

import com.example.ttcs.entity.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CauHoiRepository extends JpaRepository<CauHoi, Integer> {
}