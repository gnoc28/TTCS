package com.example.ttcs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ttcs.entity.HocSinh;
import com.example.ttcs.service.HocSinhService; // 🚀 Nhớ import Service vào

import java.security.Principal;
import java.util.List; // 🚀 Import List để không bị lỗi

@RestController
@RequestMapping("/api/hoc-sinh")
// 🚀 ĐÃ XÓA @PreAuthorize("hasRole('HS')") Ở ĐÂY (Mở cửa cho cả GV vào)
public class HocSinhController {

    // 🚀 Bổ sung thêm dòng này để lấy dữ liệu từ DB
    @Autowired
    private HocSinhService hocSinhService;

    // 1. Học sinh nộp bài tập
    @PostMapping("/nop-bai/{giaoChoLopId}")
    @PreAuthorize("hasRole('HS')") // 🚀 Khóa riêng phòng này: Chỉ HS được vào
    public ResponseEntity<?> nopBaiTap(
            @PathVariable Integer giaoChoLopId,
            @RequestBody String linkBaiLam,
            @SuppressWarnings("unused") Principal principal) { 

        // Tạm thời comment logic của ông lại vì chưa cần thiết
        // String tenDangNhap = principal.getName();
        return ResponseEntity.ok("Nộp bài thành công!");
    }

    // 2. Xem điểm của bản thân
    @GetMapping("/xem-diem")
    @PreAuthorize("hasRole('HS')") // 🚀 Khóa riêng phòng này: Chỉ HS được vào
    public ResponseEntity<?> xemDiemCaNhan(@SuppressWarnings("unused") Principal principal) {
        // String tenDangNhap = principal.getName();
        return ResponseEntity.ok("Đây là bảng điểm của riêng bạn!");
    }

    // 3. 🚀 Lấy toàn bộ danh sách học sinh từ Database
    @GetMapping("/danh-sach")
    @PreAuthorize("hasRole('GV') or hasRole('ADMIN')") // 🚀 Khóa phòng này: Chỉ GV hoặc ADMIN được vào
    public ResponseEntity<?> layTatCaHocSinh() {
        // Gọi xuống Service để lấy dữ liệu
        List<HocSinh> danhSachHS = hocSinhService.layTatCaHocSinh(); 
        return ResponseEntity.ok(danhSachHS);
    }
}