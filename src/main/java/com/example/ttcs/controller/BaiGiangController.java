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

    @Autowired private BaiGiangRepository baiGiangRepo;
    @Autowired private FileBaiGiangRepository fileRepo;
    @Autowired private BaiGiangLopHocRepository bgLopHocRepo;
    @Autowired private GiaoVienRepository giaoVienRepo;
    @Autowired private LopHocRepository lopHocRepo;

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
    // API 3: CHO HỌC SINH - Lấy danh sách bài giảng theo Lớp
    // ==========================================
    @GetMapping("/lop-hoc/{idLopHoc}")
    public ResponseEntity<?> layBaiGiangCuaLop(@PathVariable Integer idLopHoc) {
        try {
            var danhSachGiao = bgLopHocRepo.findByLopHocId(idLopHoc);
            java.util.List<BaiGiang> danhSachBaiGiang = new java.util.ArrayList<>();
            for (BaiGiangLopHoc bglh : danhSachGiao) {
                danhSachBaiGiang.add(bglh.getBaiGiang());
            }
            return ResponseEntity.ok(danhSachBaiGiang);
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
            // Lưu ý: Nếu idBaiGiang trong repository của bạn bắt buộc là Long, hãy đổi lại thành Long ở đây
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
}