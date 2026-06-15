package com.example.ttcs.controller;

import com.example.ttcs.entity.GiaoVien;
import com.example.ttcs.entity.GiaoVienMonHoc;
import com.example.ttcs.entity.NguoiDung;
import com.example.ttcs.entity.Truong;
import com.example.ttcs.enums.GioiTinh;
import com.example.ttcs.repository.GiaoVienMonHocRepository;
import com.example.ttcs.repository.GiaoVienRepository;
import com.example.ttcs.repository.HocSinhRepository;
import com.example.ttcs.repository.NguoiDungRepository;
import com.example.ttcs.repository.TruongRepository;
import com.example.ttcs.repository.MonHocRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class NguoiDungController {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TruongRepository truongRepository;
    @Autowired
    private MonHocRepository monHocRepository;
    @Autowired
    private GiaoVienRepository giaoVienRepository;
    @Autowired
    private HocSinhRepository hocSinhRepository;
    @Autowired
    private GiaoVienMonHocRepository giaoVienMonHocRepository;

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

    // PUT /users/me
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestBody Map<String, Object> body) {
        NguoiDung nd = getCurrentUser();
        if (nd == null)
            return ResponseEntity.notFound().build();

        if (body.get("ten") != null)
            nd.setTen((String) body.get("ten"));

        if (body.get("soDienThoai") != null)
            nd.setSoDienThoai((String) body.get("soDienThoai"));

        if (body.get("ngaySinh") != null) {
            try {
                nd.setNgaySinh(LocalDate.parse((String) body.get("ngaySinh")));
            } catch (Exception ignored) {
            }
        }

        if (body.get("gioiTinh") != null) {
            try {
                nd.setGioiTinh(GioiTinh.valueOf((String) body.get("gioiTinh")));
            } catch (Exception ignored) {
            }
        }

        if (body.get("anhDaiDien") != null)
            nd.setAnhDaiDien((String) body.get("anhDaiDien"));

        nguoiDungRepository.save(nd);

        String truongTen = (String) body.getOrDefault("truongTen", "");
        String truongTinh = (String) body.getOrDefault("truongTinh", "");
        String truongXa = (String) body.getOrDefault("truongXa", "");

        if (truongTen != null && !truongTen.isBlank()) {
            String ten = truongTen.trim();
            String tinh = truongTinh != null ? truongTinh.trim() : "";
            String xa = truongXa != null ? truongXa.trim() : "";

            Truong truong = truongRepository.findByTenAndTinhAndXa(ten, tinh, xa)
                    .orElseGet(() -> {
                        Truong t = new Truong();
                        t.setTen(ten);
                        t.setTinh(tinh.isBlank() ? null : tinh);
                        t.setXa(xa.isBlank() ? null : xa);
                        return truongRepository.save(t);
                    });

            switch (nd.getVaiTro()) {
                case GV -> giaoVienRepository.findById(nd.getId()).ifPresent(gv -> {
                    gv.setTruong(truong);
                    giaoVienRepository.save(gv);
                });
                case HS -> hocSinhRepository.findById(nd.getId()).ifPresent(hs -> {
                    hs.setTruong(truong);
                    hocSinhRepository.save(hs);
                });
                default -> {
                }
            }
        }

        nd.setMatKhau(null);
        return ResponseEntity.ok(nd);
    }

    // PUT /users/me/password
    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        NguoiDung nd = getCurrentUser();
        if (nd == null)
            return ResponseEntity.notFound().build();

        String matKhauCu = body.get("matKhauCu");
        String matKhauMoi = body.get("matKhauMoi");

        if (!passwordEncoder.matches(matKhauCu, nd.getMatKhau()))
            return ResponseEntity.badRequest().body("Mật khẩu hiện tại không đúng!");

        nd.setMatKhau(passwordEncoder.encode(matKhauMoi));
        nguoiDungRepository.save(nd);
        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }

    // Helper
    private NguoiDung getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return nguoiDungRepository.findByTenDangNhap(auth.getName()).orElse(null);
    }

    // GET /users/truong/search?keyword=...
    @GetMapping("/truong/search")
    public ResponseEntity<?> searchTruong(@RequestParam String keyword) {
        if (keyword == null || keyword.isBlank())
            return ResponseEntity.ok(java.util.List.of());
        return ResponseEntity.ok(truongRepository.searchByTen(keyword.trim()));
    }

    @GetMapping("/giao-vien/mon-hoc")
    public ResponseEntity<?> getMonHocDangDay() {
        NguoiDung nd = getCurrentUser();
        if (nd == null)
            return ResponseEntity.notFound().build();

        var result = giaoVienMonHocRepository.findByGiaoVienId(nd.getId())
                .stream().map(gvm -> {
                    java.util.Map<String, Object> m = new java.util.HashMap<>();
                    m.put("monHocId", gvm.getMonHoc().getId());
                    m.put("monHocTen", gvm.getMonHoc().getTen());
                    m.put("khoiLopId", gvm.getMonHoc().getKhoiLop().getId());
                    m.put("khoiLopTen", gvm.getMonHoc().getKhoiLop().getTen());
                    return m;
                }).toList();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/giao-vien/mon-hoc")
    @Transactional
    public ResponseEntity<?> saveMonHocDangDay(@RequestBody List<Integer> monHocIds) {
        NguoiDung nd = getCurrentUser();
        if (nd == null)
            return ResponseEntity.notFound().build();

        GiaoVien gv = giaoVienRepository.findById(nd.getId()).orElse(null);
        if (gv == null)
            return ResponseEntity.badRequest().body("Không tìm thấy giáo viên!");

        giaoVienMonHocRepository.deleteByGiaoVienId(nd.getId());

        for (Integer monHocId : monHocIds) {
            monHocRepository.findById(monHocId).ifPresent(monHoc -> {
                GiaoVienMonHoc gvm = new GiaoVienMonHoc();
                gvm.setGiaoVien(gv);
                gvm.setMonHoc(monHoc);
                giaoVienMonHocRepository.save(gvm);
            });
        }

        return ResponseEntity.ok("Cập nhật môn dạy thành công!");
    }
}