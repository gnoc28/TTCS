package com.example.ttcs.repository;

import com.example.ttcs.entity.De;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeRepository extends JpaRepository<De, Integer> {
}