package com.example.ttcs.controller;

import com.example.ttcs.dto.LopHocRequest;
import com.example.ttcs.entity.HocSinh;
import com.example.ttcs.entity.LopHoc;
import com.example.ttcs.repository.HocSinhRepository;
import com.example.ttcs.service.LopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/lop-hoc")
@CrossOrigin(origins = "*")
public class LopHocController {

    @Autowired
    private LopHocService lopHocService;
    @Autowired
    private HocSinhRepository hocSinhRepository;

    @PostMapping("/tao-moi")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> taoLopHoc(@RequestBody LopHocRequest request) {
        Integer currentGiaoVienId = request.getGiaoVienId();
        LopHoc lopHocMoi = lopHocService.taoLopHoc(request, currentGiaoVienId);
        return ResponseEntity.ok(lopHocMoi);
    }

    @DeleteMapping("/{lopHocId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> xoaLopHoc(@PathVariable Integer lopHocId) {
        try {
            lopHocService.xoaLopHoc(lopHocId);
            return ResponseEntity.ok("Đã xóa lớp học thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{lopHocId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> suaLopHoc(
            @PathVariable Integer lopHocId,
            @RequestBody LopHocRequest request) {
        try {
            return ResponseEntity.ok(lopHocService.suaLopHoc(lopHocId, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // tim kiem hoc sinh theo ten hoac ma hoc sinh
    @GetMapping("/hoc-sinh/search")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> searchHocSinh(@RequestParam String keyword) {
        if (keyword == null || keyword.isBlank())
            return ResponseEntity.ok(List.of());

        List<HocSinh> results = hocSinhRepository
                .findByMaHSContainingIgnoreCaseOrNguoiDung_TenContainingIgnoreCase(
                        keyword.trim(), keyword.trim());

        List<Map<String, Object>> response = results.stream().map(hs -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", hs.getId());
            m.put("maHS", hs.getMaHS());
            m.put("ten", hs.getNguoiDung().getTen());
            m.put("email", hs.getNguoiDung().getEmail());
            m.put("anhDaiDien", hs.getNguoiDung().getAnhDaiDien());
            m.put("truong", hs.getTruong() != null ? hs.getTruong().getTen() : null);
            return m;
        }).toList();

        return ResponseEntity.ok(response);
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

    // Xóa học sinh khỏi lớp
    @PostMapping("/{lopHocId}/xoa-hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> xoaHocSinhReact(
            @PathVariable Integer lopHocId,
            @PathVariable Integer hocSinhId) {

        lopHocService.xoaHocSinhKhoiLopReact(lopHocId, hocSinhId);
        return ResponseEntity.ok("Xóa học sinh thành công!");
    }

    // lấy danh sách lớp của giáo viên
    // @GetMapping("/giao-vien/{giaoVienId}")
    // @PreAuthorize("hasRole('GV')")
    // public ResponseEntity<?> layDanhSachLopTheoGiaoVien(@PathVariable Integer giaoVienId) {
    //     return ResponseEntity.ok(lopHocService.layDanhSachLopTheoGiaoVienId(giaoVienId));
    // }
    
    @GetMapping("/giao-vien/{giaoVienId}")
    @PreAuthorize("hasRole('GV')")
    public ResponseEntity<?> layDanhSachLopTheoGiaoVien(@PathVariable Integer giaoVienId) {
        return ResponseEntity.ok(lopHocService.layDanhSachLopTheoGiaoVienIdCoSiSo(giaoVienId));
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

        // Gọi thẳng xuống Service để lưu vào Database
        lopHocService.themHocSinhBangMa(lopHocId, maHocSinh);

        return ResponseEntity.ok("Thêm học sinh thành công vào DB!");
    }

    // hoc sinh
    @GetMapping("/hoc-sinh/{hocSinhId}")
    @PreAuthorize("hasRole('HS')")
    public ResponseEntity<?> layDanhSachLopTheoHocSinh(@PathVariable Integer hocSinhId) {
        return ResponseEntity.ok(lopHocService.layDanhSachLopTheoHocSinhId(hocSinhId));
    }

    @GetMapping("/{lopHocId}")
    @PreAuthorize("hasAnyRole('GV', 'HS')")
    public ResponseEntity<?> layThongTinLop(@PathVariable Integer lopHocId) {
        return ResponseEntity.ok(lopHocService.layThongTinLop(lopHocId));
    }
}