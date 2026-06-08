package com.example.ttcs.controller;

import com.example.ttcs.dto.request.DiemRequest;
import com.example.ttcs.dto.request.ThongKeRequest;
import com.example.ttcs.dto.response.ChiTietKetQuaResponse;
import com.example.ttcs.dto.response.KetQuaResponse;
import com.example.ttcs.dto.response.ThongKeResponse; // Đã thêm import này
import com.example.ttcs.service.ChiTietKetQuaService;
import com.example.ttcs.service.KetQuaService;
import com.example.ttcs.service.ThongKeService;
import com.example.ttcs.service.FeedbackGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ket-qua")
public class KetQuaController {

    @Autowired
    private KetQuaService ketQuaService;

    @Autowired
    private ChiTietKetQuaService chiTietKetQuaService;

    @Autowired
    private ThongKeService thongKeService;

    @Autowired
    private FeedbackGenerationService feedbackGenerationService;

    @PostMapping("/submit")
    public ResponseEntity<KetQuaResponse> chamDiem(@RequestBody DiemRequest request) {
        return ResponseEntity.ok(ketQuaService.chamDiem(request));
    }

    @GetMapping("/{id}/chi-tiet")
    public ResponseEntity<ChiTietKetQuaResponse> chiTiet(@PathVariable Integer id) {
        return ResponseEntity.ok(chiTietKetQuaService.getChiTietKetQua(id));
    }

    @PostMapping("/thong-ke")
    @PreAuthorize("hasAnyRole('GV', 'ADMIN')")
    public ResponseEntity<?> thongKeKetQuaTheoDeVaLopOrHocSinhLop(@RequestBody ThongKeRequest req) {
        // 1. Hứng trọn vẹn cục dữ liệu chuẩn của Quyết
        ThongKeResponse response = thongKeService.thongKeKetQuaTheoDeVaLopOrHocSinhLop(req);

        // 2. Gọi AI sinh nhận xét
        String teacherAdvice = feedbackGenerationService.generateTeacherAdvice(-1, -1, -1, -1);

        // 3. Nhét lời khuyên vào cùng cấp với tieuDe
        response.teacherAdvice = teacherAdvice;

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/giao-vien-nhan-xet")
    @PreAuthorize("hasAnyRole('GV', 'ADMIN')")
    public ResponseEntity<?> luuNhanXetGiaoVien(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String nhanXet = body.get("nhanXetGiaoVien");
        ketQuaService.luuNhanXetGiaoVien(id, nhanXet);
        
        return ResponseEntity.ok(Map.of("message", "Lưu nhận xét thành công!"));
    }
}