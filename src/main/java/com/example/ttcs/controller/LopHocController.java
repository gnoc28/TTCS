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
    @PreAuthorize("hasRole('GV')") 
    public ResponseEntity<?> taoLopHoc(@RequestBody LopHocRequest request) {
        Integer currentGiaoVienId = request.getGiaoVienId(); 
        LopHoc lopHocMoi = lopHocService.taoLopHoc(request, currentGiaoVienId);
        return ResponseEntity.ok(lopHocMoi);
    }

    @PostMapping("/{lopHocId}/them-hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> themHocSinh(
            @PathVariable Integer lopHocId, 
            @PathVariable Integer hocSinhId,
            @RequestParam Integer giaoVienId) { 
        
        lopHocService.themHocSinhVaoLop(lopHocId, hocSinhId, giaoVienId);
        return ResponseEntity.ok("Thêm học sinh thành công!");
    }

    // 🚀 Xóa học sinh khỏi lớp (Đã bỏ yêu cầu giaoVienId cho gọn)
    @PostMapping("/{lopHocId}/xoa-hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> xoaHocSinhReact(
            @PathVariable Integer lopHocId, 
            @PathVariable Integer hocSinhId) { 
        
        lopHocService.xoaHocSinhKhoiLopReact(lopHocId, hocSinhId);
        return ResponseEntity.ok("Xóa học sinh thành công!");
    }

    @GetMapping("/giao-vien/{giaoVienId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> layDanhSachLopTheoGiaoVien(@PathVariable Integer giaoVienId) {
        return ResponseEntity.ok(lopHocService.layDanhSachLopTheoGiaoVienId(giaoVienId));
    }
   // Lấy danh sách học sinh
    @GetMapping("/{lopHocId}/hoc-sinh")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> layDanhSachHocSinhCuaLop(@PathVariable Integer lopHocId) {
        // Đã gọi được hàm Service vừa viết
        return ResponseEntity.ok(lopHocService.layDanhSachHocSinhTheoLop(lopHocId)); 
    }

    // Thêm học sinh bằng mã
    @PostMapping("/{lopHocId}/them-hoc-sinh-bang-ma")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> themHocSinhBangMa(
            @PathVariable Integer lopHocId,
            @RequestBody java.util.Map<String, String> body) {
        
        String maHocSinh = body.get("studentCode");
        
        // 🚀 Lắp đạn thật: Gọi thẳng xuống Service để lưu vào Database
        lopHocService.themHocSinhBangMa(lopHocId, maHocSinh);
        
        return ResponseEntity.ok("Thêm học sinh thành công vào DB!");
    }
}