package com.example.ttcs.security;

import com.example.ttcs.entity.NguoiDung;
import com.example.ttcs.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));

        // 🚀 1. Lấy tên quyền gốc từ Database
        String tenQuyenDb = nguoiDung.getVaiTro().name(); 
        
        // 🚀 2. Dịch thuật quyền (Ép về chuẩn GV hoặc HS)
        String quyenChuan = tenQuyenDb;
        if (tenQuyenDb.equals("GIAO_VIEN") || tenQuyenDb.equals("TEACHER")) {
            quyenChuan = "GV";
        } else if (tenQuyenDb.equals("HOC_SINH") || tenQuyenDb.equals("STUDENT")) {
            quyenChuan = "HS";
        }

        // 🚀 3. Cấp vé cho Spring Security (Tự động thêm tiền tố ROLE_)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + quyenChuan);
        
        // 🚀 4. IN RA TERMINAL ĐỂ BẮT TẬN TAY
        System.out.println("=========================================");
        System.out.println("🔥 User đang đăng nhập: " + username);
        System.out.println("🔥 Quyền gốc trong DB: " + tenQuyenDb);
        System.out.println("🔥 Quyền đã cấp thành công: " + authority.getAuthority());
        System.out.println("=========================================");

        return new User(
                nguoiDung.getTenDangNhap(),
                nguoiDung.getMatKhau(),
                Collections.singletonList(authority));
    }
}