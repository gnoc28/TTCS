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

    // Hàm phân quyền: Đảm bảo chỉ GV tạo lớp mới được quyền sửa lớp đó
    private LopHoc kiemTraQuyenGiaoVien(Integer lopHocId, Integer giaoVienId) {
        LopHoc lopHoc = lopHocRepository.findById(lopHocId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));
        if (!lopHoc.getGiaoVien().getId().equals(giaoVienId)) {
            throw new RuntimeException("Từ chối truy cập: Bạn không có quyền thao tác trên lớp học này");
        }
        return lopHoc;
    }
}