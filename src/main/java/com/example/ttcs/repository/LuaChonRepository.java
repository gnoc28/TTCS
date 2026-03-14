package com.example.ttcs.repository;

import com.example.ttcs.entity.LuaChon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuaChonRepository extends JpaRepository<LuaChon, Integer> {
}