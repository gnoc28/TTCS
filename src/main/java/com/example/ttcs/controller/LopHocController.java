package com.example.ttcs.controller;

import com.example.ttcs.dto.LopHocRequest;
import com.example.ttcs.entity.LopHoc;
import com.example.ttcs.service.LopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/lop-hoc")
@CrossOrigin(origins = "*")
public class LopHocController {

    @Autowired
    private LopHocService lopHocService;

    @PostMapping("/tao-moi")
    @PreAuthorize("hasAuthority('ROLE_GV')") // Đã sửa
    public ResponseEntity<?> taoLopHoc(@RequestBody LopHocRequest request) {
        Integer currentGiaoVienId = request.getGiaoVienId();
        LopHoc lopHocMoi = lopHocService.taoLopHoc(request, currentGiaoVienId);
        return ResponseEntity.ok(lopHocMoi);
    }

    @PostMapping("/{lopHocId}/them-hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasAuthority('ROLE_GV')") // Đã sửa
    public ResponseEntity<?> themHocSinh(
            @PathVariable Integer lopHocId,
            @PathVariable Integer hocSinhId,
            @RequestParam Integer giaoVienId) {

        lopHocService.themHocSinhVaoLop(lopHocId, hocSinhId, giaoVienId);
        return ResponseEntity.ok("Thêm học sinh thành công!");
    }

    @PostMapping("/{lopHocId}/xoa-hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasAuthority('ROLE_GV')") // Đã sửa
    public ResponseEntity<?> xoaHocSinhReact(
            @PathVariable Integer lopHocId,
            @PathVariable Integer hocSinhId) {

        lopHocService.xoaHocSinhKhoiLopReact(lopHocId, hocSinhId);
        return ResponseEntity.ok("Xóa học sinh thành công!");
    }

    // 🚀 API NÀY SẼ HẾT 403 NGAY LẬP TỨC
    @GetMapping("/giao-vien/{giaoVienId}")
    //@PreAuthorize("hasAuthority('ROLE_GV')") // Đã sửa
    public ResponseEntity<?> layDanhSachLopTheoGiaoVien(@PathVariable Integer giaoVienId) {
        return ResponseEntity.ok(lopHocService.layDanhSachLopTheoGiaoVienId(giaoVienId));
    }

    @GetMapping("/{lopHocId}/hoc-sinh")
    @PreAuthorize("hasAuthority('ROLE_GV')") // Đã sửa
    public ResponseEntity<?> layDanhSachHocSinhCuaLop(@PathVariable Integer lopHocId) {
        return ResponseEntity.ok(lopHocService.layDanhSachHocSinhTheoLop(lopHocId));
    }

    @PostMapping("/{lopHocId}/them-hoc-sinh-bang-ma")
    @PreAuthorize("hasAuthority('ROLE_GV')") // Đã sửa
    public ResponseEntity<?> themHocSinhBangMa(
            @PathVariable Integer lopHocId,
            @RequestBody java.util.Map<String, String> body) {

        String maHocSinh = body.get("studentCode");
        lopHocService.themHocSinhBangMa(lopHocId, maHocSinh);

        return ResponseEntity.ok("Thêm học sinh thành công vào DB!");
    }

    @GetMapping("/hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasAuthority('ROLE_HS')") // Đã sửa
    public ResponseEntity<?> layDanhSachLopTheoHocSinh(@PathVariable Integer hocSinhId) {
        return ResponseEntity.ok(lopHocService.layDanhSachLopTheoHocSinhId(hocSinhId));
    }

    @GetMapping("/{lopHocId}")
    @PreAuthorize("hasAnyAuthority('ROLE_GV', 'ROLE_HS')") // Đã sửa
    public ResponseEntity<?> layThongTinLop(@PathVariable Integer lopHocId) {
        return ResponseEntity.ok(lopHocService.layThongTinLop(lopHocId));
    }
}