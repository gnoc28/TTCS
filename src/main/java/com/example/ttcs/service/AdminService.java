package com.example.ttcs.service;

import com.example.ttcs.entity.*;
import com.example.ttcs.enums.VaiTro;
import com.example.ttcs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private GiaoVienRepository giaoVienRepository;

    @Autowired
    private HocSinhRepository hocSinhRepository;

    // --- De ---
    @Autowired
    private DeRepository deRepository;

    @Autowired
    private CauHoiRepository cauHoiRepository;

    @Autowired
    private LuaChonRepository luaChonRepository;

    @Autowired
    private KetQuaRepository ketQuaRepository;

    @Autowired
    private ChiTietKetQuaRepository chiTietKetQuaRepository;

    @Autowired
    private GiaoChoLopRepository giaoChoLopRepository;

    // --- BaiGiang ---
    @Autowired
    private BaiGiangRepository baiGiangRepository;

    @Autowired
    private FileBaiGiangRepository fileBaiGiangRepository;

    @Autowired
    private BaiGiangLopHocRepository baiGiangLopHocRepository;

    // --- LopHoc ---
    @Autowired
    private LopHocRepository lopHocRepository;

    @Autowired
    private HocSinhLopRepository hocSinhLopRepository;

    // --- GiaoVienMonHoc ---
    @Autowired
    private GiaoVienMonHocRepository giaoVienMonHocRepository;

    // =========================================================

    public List<NguoiDung> danhSachTaiKhoan() {
        return nguoiDungRepository.findAll()
                .stream()
                .filter(u -> u.getVaiTro() != VaiTro.ADMIN)
                .peek(u -> u.setMatKhau(null))
                .toList();
    }

    @Transactional
    public void xoaTaiKhoan(Integer id) {
        NguoiDung u = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (u.getVaiTro() == VaiTro.ADMIN)
            throw new RuntimeException("Không thể xóa tài khoản Admin!");

        if (u.getVaiTro() == VaiTro.GV) {
            xoaDuLieuGiaoVien(id);
            giaoVienRepository.deleteById(id);
        } else if (u.getVaiTro() == VaiTro.HS) {
            xoaDuLieuHocSinh(id);
            hocSinhRepository.deleteById(id);
        }

        nguoiDungRepository.deleteById(id);
    }

    // =========================================================
    // PRIVATE — cascade theo từng vai trò
    // =========================================================

    // Xóa toàn bộ dữ liệu của Giáo viên (pattern giống deleteExam)
    private void xoaDuLieuGiaoVien(Integer giaoVienId) {

        // 1. Cascade xóa tất cả Đề GV đã tạo
        List<De> danhSachDe = deRepository.findByNguoiTaoIdOrderByUpdatedAtDesc(giaoVienId);
        for (De de : danhSachDe) {
            Integer deId = de.getId();
            chiTietKetQuaRepository.deleteByCauHoiDeId(deId);
            ketQuaRepository.deleteByDeId(deId);
            giaoChoLopRepository.deleteByDeId(deId);
            luaChonRepository.deleteByCauHoiDeId(deId);
            cauHoiRepository.deleteByDeId(deId);
            deRepository.deleteById(deId);
        }

        // 2. Cascade xóa tất cả Bài giảng GV đã tạo
        List<BaiGiang> danhSachBg = baiGiangRepository.findByGiaoVienId(giaoVienId);
        for (BaiGiang bg : danhSachBg) {
            Integer bgId = bg.getId();
            fileBaiGiangRepository.deleteAll(fileBaiGiangRepository.findByBaiGiangId(bgId));
            baiGiangLopHocRepository.deleteByBaiGiangId(bgId);
            baiGiangRepository.deleteById(bgId);
        }

        // 3. Cascade xóa tất cả Lớp học GV đã tạo
        List<LopHoc> danhSachLop = lopHocRepository.findByGiaoVienId(giaoVienId);
        for (LopHoc lop : danhSachLop) {
            Integer lopId = lop.getId();
            // KetQua của HS trong lớp này (qua HocSinhLop)
            chiTietKetQuaRepository.deleteByKetQuaHocSinhLopLopHocId(lopId);
            ketQuaRepository.deleteByHocSinhLopLopHocId(lopId);
            hocSinhLopRepository.deleteByLopHocId(lopId);
            // GiaoChoLop từ đề khác vẫn trỏ vào lớp này
            giaoChoLopRepository.deleteByLopHocId(lopId);
            baiGiangLopHocRepository.deleteByLopHocId(lopId);
            lopHocRepository.deleteById(lopId);
        }

        // 4. Xóa môn học đang dạy
        giaoVienMonHocRepository.deleteByGiaoVienId(giaoVienId);
    }

    // Xóa toàn bộ dữ liệu của Học sinh
    private void xoaDuLieuHocSinh(Integer hocSinhId) {

        // 1. Cascade xóa Đề HS tự tạo (phamViGiao = TU)
        List<De> danhSachDe = deRepository.findByNguoiTaoIdOrderByUpdatedAtDesc(hocSinhId);
        for (De de : danhSachDe) {
            Integer deId = de.getId();
            chiTietKetQuaRepository.deleteByCauHoiDeId(deId);
            ketQuaRepository.deleteByDeId(deId);
            giaoChoLopRepository.deleteByDeId(deId);
            luaChonRepository.deleteByCauHoiDeId(deId);
            cauHoiRepository.deleteByDeId(deId);
            deRepository.deleteById(deId);
        }

        // 2. Xóa KetQua + ChiTietKetQua của HS
        chiTietKetQuaRepository.deleteByKetQuaHocSinhId(hocSinhId);
        ketQuaRepository.deleteByHocSinhId(hocSinhId);

        // 3. Xóa HocSinhLop
        hocSinhLopRepository.deleteByHocSinhId(hocSinhId);
    }

    public NguoiDung doiVaiTro(Integer id, String vaiTroMoi) {
        NguoiDung u = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (u.getVaiTro() == VaiTro.ADMIN)
            throw new RuntimeException("Không thể đổi vai trò Admin!");

        u.setVaiTro(VaiTro.valueOf(vaiTroMoi));
        nguoiDungRepository.save(u);
        u.setMatKhau(null);
        return u;
    }
}