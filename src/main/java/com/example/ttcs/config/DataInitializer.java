package com.example.ttcs.config;

import com.example.ttcs.entity.NguoiDung;
import com.example.ttcs.enums.VaiTro;
import com.example.ttcs.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    NguoiDungRepository nguoiDungRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Chỉ tạo nếu chưa tồn tại
        if (nguoiDungRepository.findByTenDangNhap("admin").isEmpty()) {
            NguoiDung admin = new NguoiDung();
            admin.setTenDangNhap("admin");
            admin.setMatKhau(passwordEncoder.encode("123456"));
            admin.setTen("Admin");
            admin.setEmail("admin@ttcs.com");
            admin.setVaiTro(VaiTro.ADMIN);
            nguoiDungRepository.save(admin);
            System.out.println("Đã tạo tài khoản admin mặc định");
        }
    }
}