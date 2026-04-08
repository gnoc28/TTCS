package com.example.ttcs.controller;

import com.example.ttcs.dto.CreateExamRequest;
import com.example.ttcs.dto.ExamConfigResponse;
import com.example.ttcs.dto.ExamContentResponse;
import com.example.ttcs.dto.LopHocRef;
import com.example.ttcs.dto.PreviewExamDTO;
import com.example.ttcs.dto.PublishExamRequest;
import com.example.ttcs.service.CreateExamService;
import com.example.ttcs.service.ExamInforService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExamController {
        // create exam
        private final CreateExamService createExamService;
        private final ExamInforService examInforService;

        @PostMapping("/create_exam")
        public ResponseEntity<?> createExam(
                        @RequestBody CreateExamRequest request,
                        @AuthenticationPrincipal UserDetails userDetails) {
                // Chỉ lấy username rồi truyền xuống service — không dùng repo ở đây
                String username = userDetails.getUsername();

                Integer deId = createExamService.createExam(request, username);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(Map.of("id", deId, "message", "Tạo đề thành công"));
        }

        // preview exam

        // GET /create_exam/previewsExamList
        @GetMapping("/create_exam/previewsExamList")
        public ResponseEntity<List<PreviewExamDTO>> getPreviewsExamList(
                        @AuthenticationPrincipal UserDetails userDetails) {

                String username = userDetails.getUsername();
                List<PreviewExamDTO> result = createExamService.getPreviewsExamList(username);

                return ResponseEntity.ok(result);
        }

        @GetMapping("/create_exam/{id}/config")
        public ResponseEntity<?> getConfig(
                        @PathVariable Integer id,
                        @AuthenticationPrincipal UserDetails userDetails) {

                String username = userDetails.getUsername();
                ExamConfigResponse response = examInforService.getConfig(id, username);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/create_exam/{id}/content")
        public ResponseEntity<?> getContent(
                        @PathVariable Integer id,
                        @AuthenticationPrincipal UserDetails userDetails) {

                String username = userDetails.getUsername();
                ExamContentResponse response = examInforService.getContent(id, username);
                return ResponseEntity.ok(response);
        }

        @PostMapping("/create_exam/{id}/publish")
        public ResponseEntity<?> publishExam(
                        @PathVariable Integer id,
                        @RequestBody PublishExamRequest request,
                        @AuthenticationPrincipal UserDetails userDetails) {

                String username = userDetails.getUsername();
                ExamConfigResponse response = examInforService.publish(id, username, request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        // GET /create_exam/my-classes — tất cả lớp của giáo viên đang đăng nhập
        @GetMapping("/create_exam/my-classes")
        public ResponseEntity<?> getTatCaLopCuaGV(
                        @AuthenticationPrincipal UserDetails userDetails) {

                String username = userDetails.getUsername();
                List<LopHocRef> result = examInforService.getTatCaLopCuaGV(username);
                return ResponseEntity.ok(result);
        }

        @DeleteMapping("/create_exam/{id}")
        public ResponseEntity<?> deleteExam(@PathVariable Integer id,
                        @AuthenticationPrincipal UserDetails userDetails) {
                createExamService.deleteExam(id, userDetails.getUsername());
                return ResponseEntity.ok().build();
        }
}
// POST/create_exam(JWT→username)│▼ExamController└─

// lấy username từ
// @AuthenticationPrincipal
// →truyền xuống service│▼ExamService.createExam(request,username)├─

// findByTenDangNhap(username) → lấy NguoiDung
// ├─ xác định phamViGiao (GV→LOP, HS→TU)
// ├─ sinh maHash 6 ký tự unique
// ├─ INSERT de ←──────────── deRepository.save()
// └─ for mỗi cauHoi trong noiDungDe.cauHois
// ├─ INSERT cau_hoi ←── cauHoiRepository.save()
// └─ for mỗi luaChon
// └─ INSERT lua_chon ← luaChonRepository.save()