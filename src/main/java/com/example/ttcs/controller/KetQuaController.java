package com.example.ttcs.controller;

import com.example.ttcs.dto.request.DiemRequest;
import com.example.ttcs.dto.request.ThongKeRequest;
import com.example.ttcs.dto.response.ThongKeResponse;
import com.example.ttcs.dto.response.ChiTietKetQuaResponse;
import com.example.ttcs.dto.response.KetQuaResponse;
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
        ThongKeResponse response = thongKeService.thongKeKetQuaTheoDeVaLopOrHocSinhLop(req);

        // Gọi sinh nhận xét cho giáo viên
        Map<String, Object> tk = response.thongKe;

        double avgNB = ((Number) tk.getOrDefault("avgNB", -1))
                .doubleValue();

        double avgTH = ((Number) tk.getOrDefault("avgTH", -1))
                .doubleValue();

        double avgVD = ((Number) tk.getOrDefault("avgVD", -1))
                .doubleValue();

        double avgVDC = ((Number) tk.getOrDefault("avgVDC", -1))
                .doubleValue();

        String teacherAdvice = feedbackGenerationService.generateTeacherAdvice(
                avgNB,
                avgTH,
                avgVD,
                avgVDC);

        response.teacherAdvice = teacherAdvice;

        // Thêm nhận xét cho giáo viên
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

    // GET /api/ket-qua/{id}/info — lấy thông tin + feedback đầy đủ
    @GetMapping("/{id}/info")
    public ResponseEntity<?> getKetQuaInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(ketQuaService.getKetQuaInfo(id));
    }

    // GET /api/ket-qua/lich-su?hocSinhId=X&deId=Y — lịch sử làm bài
    @GetMapping("/lich-su")
    public ResponseEntity<?> lichSuLamBai(
            @RequestParam Integer hocSinhId,
            @RequestParam Integer deId) {
        return ResponseEntity.ok(ketQuaService.getLichSuLamBai(hocSinhId, deId));
    }
}