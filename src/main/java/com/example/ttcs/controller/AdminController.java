package com.example.ttcs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ttcs.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

// @RestController
// @RequestMapping("/api/admin")
// @PreAuthorize("hasRole('ADMIN')") // Phân quyền toàn bộ file này chỉ Admin được vào
// public class AdminController {

//     // 1. Khóa/Mở khóa tài khoản
//     @PutMapping("/tai-khoan/{nguoiDungId}/trang-thai")
//     public ResponseEntity<?> thayDoiTrangThaiTaiKhoan(@PathVariable Integer nguoiDungId,
//             @RequestParam boolean isLocked) {
//         // Gọi NguoiDungService để sửa trạng thái trong DB
//         return ResponseEntity.ok("Đã thay đổi trạng thái tài khoản thành công!");
//     }

//     // 2. Phân quyền cho user (Ví dụ nâng học sinh lên làm giáo viên)
//     @PutMapping("/tai-khoan/{nguoiDungId}/phan-quyen")
//     public ResponseEntity<?> capNhatQuyen(@PathVariable Integer nguoiDungId, @RequestParam String roleMoi) {
//         // Gọi logic cấp quyền
//         return ResponseEntity.ok("Đã cấp quyền " + roleMoi + " cho người dùng!");
//     }
// }

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/tai-khoan")
    public ResponseEntity<?> danhSachTaiKhoan() {
        return ResponseEntity.ok(adminService.danhSachTaiKhoan());
    }

    @DeleteMapping("/tai-khoan/{id}")
    public ResponseEntity<?> xoaTaiKhoan(@PathVariable Integer id) {
        try {
            adminService.xoaTaiKhoan(id);
            return ResponseEntity.ok("Đã xóa tài khoản!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/tai-khoan/{id}/vai-tro")
    public ResponseEntity<?> doiVaiTro(@PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(adminService.doiVaiTro(id, body.get("vaiTro")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}