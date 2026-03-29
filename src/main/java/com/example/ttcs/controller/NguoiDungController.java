package com.example.ttcs.controller;

import com.example.ttcs.entity.NguoiDung;
import com.example.ttcs.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class NguoiDungController {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // GET /users/me — lấy thông tin user đang đăng nhập
    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tenDangNhap = authentication.getName();

        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(tenDangNhap)
                .orElse(null);

        if (nguoiDung == null) {
            return ResponseEntity.notFound().build();
        }

        // Không trả về matKhau
        nguoiDung.setMatKhau(null);
        return ResponseEntity.ok(nguoiDung);
    }
}