package com.example.ttcs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// PHẢI CÓ 2 DÒNG IMPORT QUAN TRỌNG NÀY:
import com.example.ttcs.dto.LoginRequest;
import com.example.ttcs.dto.SignupRequest;
import com.example.ttcs.entity.GiaoVien;
import com.example.ttcs.entity.HocSinh;
import com.example.ttcs.entity.NguoiDung;
import com.example.ttcs.enums.VaiTro; // <--- Import này để dùng HOC_SINH, GV
import com.example.ttcs.security.JwtUtils; // <--- Import này để dùng JwtUtils
import com.example.ttcs.repository.GiaoVienRepository;
import com.example.ttcs.repository.HocSinhRepository;
import com.example.ttcs.repository.NguoiDungRepository;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> userMap = new HashMap<>();
        userMap.put("vaiTro", vaiTroStr.replace("ROLE_", ""));
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

        // Lưu nguoi_dung trước để có id
        NguoiDung savedUser = nguoiDungRepository.save(user);

        // Tạo bản ghi giao_vien hoặc hoc_sinh tương ứng
        if (savedUser.getVaiTro() == VaiTro.GV) {
            GiaoVien giaoVien = new GiaoVien();
            giaoVien.setNguoiDung(savedUser);
            // maGV = "GV" + id với format 3 chữ số: GV001, GV002...
            giaoVien.setMaGV(String.format("GV%03d", savedUser.getId()));
            giaoVienRepository.save(giaoVien);

        } else if (savedUser.getVaiTro() == VaiTro.HS) {
            HocSinh hocSinh = new HocSinh();
            hocSinh.setNguoiDung(savedUser);
            // maHS = "HS" + id với format 3 chữ số: HS001, HS002...
            hocSinh.setMaHS(String.format("HS%03d", savedUser.getId()));
            hocSinhRepository.save(hocSinh);
        }

        return ResponseEntity.ok("Đăng ký thành công!");
    }
}