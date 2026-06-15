package com.example.ttcs.repository;

import com.example.ttcs.entity.De;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeRepository extends JpaRepository<De, Integer> {
    boolean existsByMaHash(String maHash);
    
    List<De> findByNguoiTaoIdOrderByUpdatedAtDesc(Integer nguoiTaoId);

}