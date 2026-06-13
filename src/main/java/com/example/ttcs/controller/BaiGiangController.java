package com.example.ttcs.controller;

import com.example.ttcs.dto.BaiGiangRequest;
import com.example.ttcs.entity.*;
import com.example.ttcs.enums.LoaiFile;
import com.example.ttcs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/baigiang")
@CrossOrigin(origins = "*")
public class BaiGiangController {

    @Autowired
    private BaiGiangRepository baiGiangRepo;
    @Autowired
    private FileBaiGiangRepository fileRepo;
    @Autowired
    private BaiGiangLopHocRepository bgLopHocRepo;
    @Autowired
    private GiaoVienRepository giaoVienRepo;
    @Autowired
    private LopHocRepository lopHocRepo;

    // ==========================================
    // API 1: TẠO MỚI BÀI GIẢNG (Đã chạy ngon)
    // ==========================================
    @PostMapping("/tao-moi")
    public ResponseEntity<?> taoBaiGiangMoi(@RequestBody BaiGiangRequest request) {
        try {
            // 1. Tìm Giáo viên
            GiaoVien gv = giaoVienRepo.findById(request.getIdGiaoVien())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Giáo viên"));

            // 2. Lưu Bài giảng
            BaiGiang bg = new BaiGiang();
            bg.setTieuDe(request.getTieuDe());
            bg.setNoiDung(request.getNoiDung());
            bg.setGiaoVien(gv);
            BaiGiang savedBg = baiGiangRepo.save(bg);

            // 3. Lưu danh sách Lớp học được gán
            if (request.getDanhSachIdLopHoc() != null) {
                for (Integer idLop : request.getDanhSachIdLopHoc()) {
                    LopHoc lop = lopHocRepo.findById(idLop).orElse(null);
                    if (lop != null) {
                        BaiGiangLopHoc bglh = new BaiGiangLopHoc();
                        bglh.setBaiGiang(savedBg);
                        bglh.setLopHoc(lop);
                        bgLopHocRepo.save(bglh);
                    }
                }
            }

            // 4. Lưu danh sách File đính kèm
            if (request.getDanhSachFile() != null) {
                for (BaiGiangRequest.FileDto fileDto : request.getDanhSachFile()) {
                    FileBaiGiang file = new FileBaiGiang();
                    file.setTieuDe(fileDto.getTenFile());
                    file.setDuongDan(fileDto.getUrlFile());

                    try {
                        file.setLoaiFile(LoaiFile.valueOf(fileDto.getLoaiFile()));
                    } catch (Exception e) {
                        file.setLoaiFile(LoaiFile.url);
                    }

                    file.setBaiGiang(savedBg);
                    fileRepo.save(file);
                }
            }

            return ResponseEntity.ok("Tạo bài giảng thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // ==========================================
    // API 2: CHO GIÁO VIÊN - Lấy danh sách bài giảng đã tạo
    // ==========================================
    @GetMapping("/giao-vien/{idGiaoVien}")
    public ResponseEntity<?> layBaiGiangCuaGiaoVien(@PathVariable Integer idGiaoVien) {
        try {
            return ResponseEntity.ok(baiGiangRepo.findByGiaoVienId(idGiaoVien));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // ==========================================
    // API 3: CHO HỌC SINH - Lấy danh sách bài giảng theo Lớp (ĐÃ FIX KÈM FILE)
    // ==========================================
    @GetMapping("/lop-hoc/{idLopHoc}")
    public ResponseEntity<?> layBaiGiangCuaLop(@PathVariable Integer idLopHoc) {
        try {
            var danhSachGiao = bgLopHocRepo.findByLopHocId(idLopHoc);

            // Tạo một danh sách các "Hộp" chứa cả Bài Giảng + File
            java.util.List<java.util.Map<String, Object>> danhSachKetQua = new java.util.ArrayList<>();

            for (BaiGiangLopHoc bglh : danhSachGiao) {
                BaiGiang bg = bglh.getBaiGiang();

                // 🚀 QUAN TRỌNG NHẤT: Bắt Java đi tìm file của bài giảng này
                var danhSachFile = fileRepo.findByBaiGiangId(bg.getId());

                // Đóng gói chúng nó lại thành 1 cục
                java.util.Map<String, Object> bgMap = new java.util.HashMap<>();
                bgMap.put("id", bg.getId());
                bgMap.put("tieuDe", bg.getTieuDe());
                bgMap.put("noiDung", bg.getNoiDung());
                // Nếu entity của ông có createdAt thì mở comment dòng dưới:
                // bgMap.put("createdAt", bg.getCreatedAt());

                // Nhét mảng file vào đây để React nhận được
                bgMap.put("danhSachFile", danhSachFile);

                danhSachKetQua.add(bgMap);
            }
            return ResponseEntity.ok(danhSachKetQua);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // ==========================================
    // API 4: DÙNG CHUNG - Xem chi tiết 1 Bài giảng (Kèm File)
    // ==========================================
    @GetMapping("/{idBaiGiang}/chi-tiet")
    public ResponseEntity<?> xemChiTietBaiGiang(@PathVariable Integer idBaiGiang) {
        try {
            // Lưu ý: Nếu idBaiGiang trong repository của bạn bắt buộc là Long, hãy đổi lại
            // thành Long ở đây
            BaiGiang bg = baiGiangRepo.findById(idBaiGiang)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bài giảng"));

            var danhSachFile = fileRepo.findByBaiGiangId(idBaiGiang);

            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("baiGiang", bg);
            response.put("taiLieuDinhKem", danhSachFile);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // ==========================================
    // API 5: XÓA BÀI GIẢNG
    // ==========================================
    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaBaiGiang(@PathVariable Integer id) {
        try {
            if (!baiGiangRepo.existsById(id)) {
                return ResponseEntity.badRequest().body("Bài giảng không tồn tại!");
            }
            baiGiangRepo.deleteById(id);
            return ResponseEntity.ok("Đã xóa bài giảng thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa: " + e.getMessage());
        }
    }

    // ==========================================
    // API 6: SỬA BÀI GIẢNG (ĐÃ FIX: Cập nhật cả File)
    // ==========================================
    @PutMapping("/sua/{id}")
    public ResponseEntity<?> suaBaiGiang(@PathVariable Integer id, @RequestBody BaiGiangRequest request) {
        try {
            BaiGiang bg = baiGiangRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Bài giảng"));

            // 1. Cập nhật Tiêu đề và Nội dung
            bg.setTieuDe(request.getTieuDe());
            bg.setNoiDung(request.getNoiDung());
            baiGiangRepo.save(bg);

            // 2. Cập nhật File: Xóa hết file cũ của bài giảng này để ghi đè cái mới
            // Tìm tất cả file cũ của bài giảng này
            var fileCu = fileRepo.findByBaiGiangId(id);
            if (fileCu != null && !fileCu.isEmpty()) {
                fileRepo.deleteAll(fileCu);
            }

            // 3. Lưu danh sách File mới (y hệt logic lúc Tạo mới)
            if (request.getDanhSachFile() != null) {
                for (BaiGiangRequest.FileDto fileDto : request.getDanhSachFile()) {
                    FileBaiGiang file = new FileBaiGiang();
                    file.setTieuDe(fileDto.getTenFile());
                    file.setDuongDan(fileDto.getUrlFile());

                    try {
                        file.setLoaiFile(com.example.ttcs.enums.LoaiFile.valueOf(fileDto.getLoaiFile()));
                    } catch (Exception e) {
                        file.setLoaiFile(com.example.ttcs.enums.LoaiFile.url);
                    }

                    file.setBaiGiang(bg);
                    fileRepo.save(file);
                }
            }

            return ResponseEntity.ok("Cập nhật bài giảng và tài liệu thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }
}