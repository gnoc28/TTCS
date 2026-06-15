package com.example.ttcs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/giao-vien")
@PreAuthorize("hasAnyRole('GV', 'ADMIN')") // Admin hoặc Giáo viên đều dùng được
public class GiaoVienController {

    // 1. Chấm điểm & Nhận xét bài làm của học sinh
    @PostMapping("/cham-diem/{ketQuaId}")
    public ResponseEntity<?> chamDiemVaNhanXet(
            @PathVariable Integer ketQuaId,
            @RequestParam Float diem,
            @RequestParam String nhanXet) {

        // Gọi KetQuaService để lưu điểm và lời phê vào DB
        return ResponseEntity.ok("Đã lưu điểm và nhận xét thành công!");
    }

    // 2. Xem thống kê kết quả học tập của 1 lớp
    @GetMapping("/thong-ke/lop/{lopHocId}")
    public ResponseEntity<?> xemThongKeLop(@PathVariable Integer lopHocId) {
        // Lấy danh sách kết quả của toàn lớp
        return ResponseEntity.ok("Dữ liệu thống kê của lớp...");
    }
    
}