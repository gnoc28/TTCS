package com.example.ttcs.controller;

import com.example.ttcs.dto.KhoiLopResponse;
import com.example.ttcs.dto.MonHocResponse;
import com.example.ttcs.entity.KhoiLop;
import com.example.ttcs.entity.MonHoc;
import com.example.ttcs.repository.KhoiLopRepository;
import com.example.ttcs.repository.MonHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class KhoiLopMonHocController {

    @Autowired
    private KhoiLopRepository khoiLopRepository;

    @Autowired
    private MonHocRepository monHocRepository;

    // GET /grades — frontend gọi GradeAPI.getAll()
    @GetMapping("/grades")
    public ResponseEntity<List<KhoiLopResponse>> getAllGrades() {
        List<KhoiLopResponse> result = khoiLopRepository.findAll()
                .stream()
                .map(khoiLop -> {
                    KhoiLopResponse dto = new KhoiLopResponse();
                    dto.setId(khoiLop.getId());
                    dto.setTen(khoiLop.getTen());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // GET /subjects/grade/{gradeId} — frontend gọi SubjectAPI.getByGradeId()
    @GetMapping("/subjects/grade/{gradeId}")
    public ResponseEntity<List<MonHocResponse>> getSubjectsByGrade(@PathVariable Integer gradeId) {
        List<MonHocResponse> result = monHocRepository.findByKhoiLopId(gradeId)
                .stream()
                .map(monHoc -> {
                    MonHocResponse dto = new MonHocResponse();
                    dto.setId(monHoc.getId());
                    dto.setTen(monHoc.getTen());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}