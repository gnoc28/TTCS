package com.example.ttcs.repository;

import com.example.ttcs.entity.GiaoChoLop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiaoChoLopRepository extends JpaRepository<GiaoChoLop, Integer> {

    List<GiaoChoLop> findByLopHoc_Id(Long lopHocId);
}