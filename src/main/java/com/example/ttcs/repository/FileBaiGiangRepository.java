package com.example.ttcs.repository;

import com.example.ttcs.entity.FileBaiGiang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileBaiGiangRepository extends JpaRepository<FileBaiGiang, Integer> {
}