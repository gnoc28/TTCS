package com.example.ttcs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.ttcs.dto.LoginRequest;
import com.example.ttcs.dto.SignupRequest;
import com.example.ttcs.entity.GiaoVien;
import com.example.ttcs.entity.HocSinh;
import com.example.ttcs.entity.NguoiDung;
import com.example.ttcs.enums.VaiTro; 
import com.example.ttcs.security.JwtUtils; 
import com.example.ttcs.repository.GiaoVienRepository;
import com.example.ttcs.repository.HocSinhRepository;
import com.example.ttcs.repository.NguoiDungRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    NguoiDungRepository nguoiDungRepository;

    @Autowired
    GiaoVienRepository giaoVienRepository;

    @Autowired
    HocSinhRepository hocSinhRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getTenDangNhap(), loginRequest.getMatKhau()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", jwt);

        String vaiTroStr = authentication.getAuthorities().iterator().next().getAuthority();
        
        // 🚀 BẮT ĐẦU NÂNG CẤP TẠI ĐÂY:
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("vaiTro", vaiTroStr.replace("ROLE_", ""));

        // 1. Dùng tên đăng nhập để tìm người dùng trong Database
        // (Lưu ý: Nếu hàm của bạn trả về Optional, hãy dùng .orElse(null) hoặc .get() tuỳ code repository của bạn)
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(loginRequest.getTenDangNhap()).orElse(null);
        // 2. Nhét ID và Tên vào Map để gửi về cho React
        if (user != null) {
            userMap.put("id", user.getId()); 
            userMap.put("tenDangNhap", user.getTenDangNhap());
        }

        response.put("user", userMap);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        NguoiDung user = new NguoiDung();
        user.setTen(signUpRequest.getTen());
        user.setTenDangNhap(signUpRequest.getTenDangNhap());
        user.setEmail(signUpRequest.getEmail());
        user.setMatKhau(passwordEncoder.encode(signUpRequest.getMatKhau()));

        if ("HS".equals(signUpRequest.getVaiTro())) {
            user.setVaiTro(VaiTro.HS);
        } else if ("GV".equals(signUpRequest.getVaiTro())) {
            user.setVaiTro(VaiTro.GV);
        } else {
            user.setVaiTro(VaiTro.HS);
        }

        NguoiDung savedUser = nguoiDungRepository.save(user);

        if (savedUser.getVaiTro() == VaiTro.GV) {
            GiaoVien giaoVien = new GiaoVien();
            giaoVien.setNguoiDung(savedUser);
            giaoVien.setMaGV(String.format("GV%03d", savedUser.getId()));
            giaoVienRepository.save(giaoVien);

        } else if (savedUser.getVaiTro() == VaiTro.HS) {
            HocSinh hocSinh = new HocSinh();
            hocSinh.setNguoiDung(savedUser);
            hocSinh.setMaHS(String.format("HS%03d", savedUser.getId()));
            hocSinhRepository.save(hocSinh);
        }

        return ResponseEntity.ok("Đăng ký thành công!");
    }
}