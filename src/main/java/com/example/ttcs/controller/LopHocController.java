package com.example.ttcs.controller;

import com.example.ttcs.dto.LopHocRequest;
import com.example.ttcs.entity.LopHoc;
import com.example.ttcs.service.LopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lop-hoc")
@CrossOrigin(origins = "*")
public class LopHocController {

    @Autowired
    private LopHocService lopHocService;

    // Phân quyền: Yêu cầu VaiTro.GV
    @PostMapping("/tao-moi")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> taoLopHoc(@RequestBody LopHocRequest request) {
        // Tạm gán giaoVienId = 1 (Sau này sẽ lấy từ Token JWT đăng nhập)
        Integer currentGiaoVienId = 1;

        LopHoc lopHocMoi = lopHocService.taoLopHoc(request, currentGiaoVienId);
        return ResponseEntity.ok(lopHocMoi);
    }

    // Phân quyền: Yêu cầu VaiTro.GV
    @PostMapping("/{lopHocId}/them-hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> themHocSinh(@PathVariable Integer lopHocId, @PathVariable Integer hocSinhId) {
        Integer currentGiaoVienId = 1;

        lopHocService.themHocSinhVaoLop(lopHocId, hocSinhId, currentGiaoVienId);
        return ResponseEntity.ok("Thêm học sinh thành công!");
    }

    // Phân quyền: Yêu cầu VaiTro.GV
    @DeleteMapping("/{lopHocId}/xoa-hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> xoaHocSinh(@PathVariable Integer lopHocId, @PathVariable Integer hocSinhId) {
        Integer currentGiaoVienId = 1;

        lopHocService.xoaHocSinhKhoiLop(lopHocId, hocSinhId, currentGiaoVienId);
        return ResponseEntity.ok("Xóa học sinh thành công!");
    }
}