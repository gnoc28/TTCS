package com.example.ttcs.controller;

import com.example.ttcs.dto.GiaoBaiRequest;
import com.example.ttcs.dto.response.DeChiTietResponse;
import com.example.ttcs.entity.De;
import com.example.ttcs.service.DeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/de-thi")
public class DeController {

    @Autowired
    private DeService deService;

    // API 1: Giáo viên giao bài (Chỉ Giáo viên hoặc Admin mới gọi được)
    @PostMapping("/giao-bai")
    @PreAuthorize("hasAnyRole('GV', 'ADMIN')")
    public ResponseEntity<?> giaoBaiChoLop(@RequestBody GiaoBaiRequest request) {
        try {
            String message = deService.giaoDeChoLop(request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API 2: Học sinh xem danh sách bài (Học sinh gọi được)
    @GetMapping("/lop/{lopHocId}")
    @PreAuthorize("hasAnyRole('HS', 'GV', 'ADMIN')")
    public ResponseEntity<?> xemBaiTapCuaLop(@PathVariable Long lopHocId) {
        List<De> danhSachDe = deService.layDanhSachBaiTapCuaLop(lopHocId);
        return ResponseEntity.ok(danhSachDe);
    }

    // API 3: Lấy chi tiết đề theo id (HS/GV/ADMIN đều xem được)
    @GetMapping("/{deId}/chi-tiet")
    @PreAuthorize("hasAnyRole('HS', 'GV', 'ADMIN')")
    public ResponseEntity<DeChiTietResponse> layChiTietDe(@PathVariable Integer deId) {
        return ResponseEntity.ok(deService.layChiTietDe(deId));
    }
}