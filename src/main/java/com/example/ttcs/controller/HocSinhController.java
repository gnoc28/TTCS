package com.example.ttcs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/hoc-sinh")
@PreAuthorize("hasRole('HS')")
public class HocSinhController {

    // 1. Học sinh nộp bài tập
    @PostMapping("/nop-bai/{giaoChoLopId}")
    public ResponseEntity<?> nopBaiTap(
            @PathVariable Integer giaoChoLopId,
            @RequestBody String linkBaiLam,
            Principal principal) { // Principal chứa thông tin user đang đăng nhập

        String tenDangNhap = principal.getName();

        // Logic: Tìm Học sinh bằng tenDangNhap -> Lưu KetQua làm bài cho học sinh đó
        return ResponseEntity.ok("Nộp bài thành công cho tài khoản: " + tenDangNhap);
    }

    // 2. Xem điểm của bản thân
    @GetMapping("/xem-diem")
    public ResponseEntity<?> xemDiemCaNhan(Principal principal) {
        String tenDangNhap = principal.getName();

        // Logic: Chỉ query trong DB lấy điểm của user có username này
        return ResponseEntity.ok("Đây là bảng điểm của riêng bạn!");
    }
}