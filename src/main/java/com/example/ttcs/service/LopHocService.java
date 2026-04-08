package com.example.ttcs.service;

import com.example.ttcs.dto.LopHocRequest;
import com.example.ttcs.entity.GiaoVien;
import com.example.ttcs.entity.HocSinh;
import com.example.ttcs.entity.HocSinhLop;
import com.example.ttcs.entity.LopHoc;
import com.example.ttcs.repository.GiaoVienRepository;
import com.example.ttcs.repository.HocSinhLopRepository;
import com.example.ttcs.repository.HocSinhRepository;
import com.example.ttcs.repository.LopHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors; // 🚀 Đã import thêm cái này

@Service
public class LopHocService {

    @Autowired
    private LopHocRepository lopHocRepository;

    @Autowired
    private HocSinhLopRepository hocSinhLopRepository;

    @Autowired
    private GiaoVienRepository giaoVienRepository;

    @Autowired
    private HocSinhRepository hocSinhRepository;

    @Transactional
    public LopHoc taoLopHoc(LopHocRequest request, Integer giaoVienId) {
        GiaoVien giaoVien = giaoVienRepository.findById(giaoVienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giáo viên"));

        LopHoc lopHoc = new LopHoc();
        lopHoc.setTenLop(request.getTenLop());
        lopHoc.setMaLop(request.getMaLop());
        lopHoc.setNamHoc(request.getNamHoc());
        lopHoc.setGiaoVien(giaoVien);

        return lopHocRepository.save(lopHoc);
    }

    @Transactional
    public void themHocSinhVaoLop(Integer lopHocId, Integer hocSinhId, Integer giaoVienId) {
        LopHoc lopHoc = kiemTraQuyenGiaoVien(lopHocId, giaoVienId);

        HocSinh hocSinh = hocSinhRepository.findById(hocSinhId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh"));

        if (hocSinhLopRepository.existsByLopHocIdAndHocSinhId(lopHocId, hocSinhId)) {
            throw new RuntimeException("Học sinh đã có trong lớp này");
        }

        HocSinhLop hocSinhLop = new HocSinhLop();
        hocSinhLop.setLopHoc(lopHoc);
        hocSinhLop.setHocSinh(hocSinh);

        hocSinhLopRepository.save(hocSinhLop);
    }

    @Transactional
    public void xoaHocSinhKhoiLop(Integer lopHocId, Integer hocSinhId, Integer giaoVienId) {
        kiemTraQuyenGiaoVien(lopHocId, giaoVienId);

        HocSinhLop hocSinhLop = hocSinhLopRepository.findByLopHocIdAndHocSinhId(lopHocId, hocSinhId)
                .orElseThrow(() -> new RuntimeException("Học sinh không thuộc lớp này"));

        hocSinhLopRepository.delete(hocSinhLop);
    }

    private LopHoc kiemTraQuyenGiaoVien(Integer lopHocId, Integer giaoVienId) {
        LopHoc lopHoc = lopHocRepository.findById(lopHocId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));
        if (!lopHoc.getGiaoVien().getId().equals(giaoVienId)) {
            throw new RuntimeException("Từ chối truy cập: Bạn không có quyền thao tác");
        }
        return lopHoc;
    }

    public List<LopHoc> layDanhSachLopTheoGiaoVienId(Integer giaoVienId) {
        return lopHocRepository.findByGiaoVienId(giaoVienId);
    }

    // =========================================================================
    // 🚀 2 HÀM MỚI ĐỂ PHỤC VỤ CHO REACT
    // =========================================================================

    // 1. Thêm học sinh thẳng bằng Mã HS (Không cần bắt React gửi GiaoVienId nữa)
    @Transactional
    public void themHocSinhBangMa(Integer lopHocId, String maHocSinh) {
        LopHoc lopHoc = lopHocRepository.findById(lopHocId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));

        // Chú ý: Dùng findByMaHs (Tìm theo mã học sinh)
        HocSinh hocSinh = hocSinhRepository.findByMaHS(maHocSinh)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh với mã: " + maHocSinh));

        if (hocSinhLopRepository.existsByLopHocIdAndHocSinhId(lopHocId, hocSinh.getId())) {
            throw new RuntimeException("Học sinh đã có trong lớp này");
        }

        HocSinhLop hocSinhLop = new HocSinhLop();
        hocSinhLop.setLopHoc(lopHoc);
        hocSinhLop.setHocSinh(hocSinh);

        hocSinhLopRepository.save(hocSinhLop);
    }

    // 2. Lấy danh sách học sinh của 1 lớp
    public List<HocSinh> layDanhSachHocSinhTheoLop(Integer lopHocId) {
        List<HocSinhLop> danhSachHocSinhLop = hocSinhLopRepository.findByLopHocId(lopHocId);
        
        // Trích xuất Học Sinh từ bảng trung gian HocSinhLop
        return danhSachHocSinhLop.stream()
                .map(HocSinhLop::getHocSinh)
                .collect(Collectors.toList());
    }
    // 3. Xóa học sinh khỏi lớp (Dành riêng cho React)
    @Transactional
    public void xoaHocSinhKhoiLopReact(Integer lopHocId, Integer hocSinhId) {
        HocSinhLop hocSinhLop = hocSinhLopRepository.findByLopHocIdAndHocSinhId(lopHocId, hocSinhId)
                .orElseThrow(() -> new RuntimeException("Học sinh không thuộc lớp này"));
        hocSinhLopRepository.delete(hocSinhLop);
    }
}