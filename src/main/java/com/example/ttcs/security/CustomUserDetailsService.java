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

        // Spring Security thường dùng tiền tố "ROLE_" để phân biệt quyền
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + nguoiDung.getVaiTro().name());

        return new User(
                nguoiDung.getTenDangNhap(),
                nguoiDung.getMatKhau(),
                Collections.singletonList(authority));
    }
}